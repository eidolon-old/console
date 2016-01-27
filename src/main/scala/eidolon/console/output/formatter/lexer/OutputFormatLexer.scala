/**
 * This file is part of the "eidolon/console" project.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the LICENSE is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package eidolon.console.output.formatter.lexer

import eidolon.console.output.formatter.lexer.token.{StyleCloseToken, StyleOpenToken, StringToken, Token}

import scala.annotation.tailrec

/**
 * OutputFormatLexer
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class OutputFormatLexer {
  /**
   * Conver the given input to a list of tokens
   *
   * @param input The input string
   * @return a list of tokens
   */
  def tokenise(input: String): List[Token] = {
    val context = new LexerContext(input.toCharArray.toList)
    val mechanism = new LexerMechanism(context)

    mechanism.tokenise()
  }

  /**
   * The lexer mechanism, an instance is made for each tokenisation
   *
   * @param context A lexer context
   */
  private class LexerMechanism(val context: LexerContext) {
    /**
     * Produce a list of tokens from the current lexer context
     *
     * @return a list of tokens
     */
    def tokenise(): List[Token] = {
      read()

      @tailrec
      def readTokens(tokens: List[Token]): List[Token] = {
        if (!isEndOfText) {
          readTokens(tokens :+ readNextToken())
        } else {
          tokens
        }
      }

      readTokens(List())
    }

    /**
     * Read the next token from the context
     *
     * @return a token
     */
    def readNextToken(): Token = {
      remainingCharacters.mkString match {
        case LexerMechanism.TagEndRegex(_, _, _) => readStyleCloseToken()
        case LexerMechanism.TagStartRegex(_, _, _) => readStyleOpenToken()
        case _ => readStringToken()
      }
    }

    /**
     * Attempt to read a style open token
     *
     * @return a style open token
     */
    def readStyleOpenToken(): StyleOpenToken = {
      read()

      while (!isEndOfText && context.current != '>') {
        context.captureBuffer.append(context.current)
        read()
      }

      read()

      new StyleOpenToken(context.flushCaptureBuffer())
    }

    /**
     * Attempt to read a style close token
     *
     * @return a style close token
     */
    def readStyleCloseToken(): StyleCloseToken = {
      read()
      read()

      while (!isEndOfText && context.current != '>') {
        context.captureBuffer.append(context.current)
        read()
      }

      read()

      new StyleCloseToken(context.flushCaptureBuffer())
    }

    /**
     * Attempt to read a string token
     *
     * @return a string token
     */
    def readStringToken(): StringToken = {
      while (!isEndOfText && isStillString) {
        context.captureBuffer.append(context.current)
        read()
      }

      new StringToken(context.flushCaptureBuffer())
    }

    /**
     * Read the next character in the context
     */
    def read(): Unit = {
      if (context.cursor >= context.buffer.length) {
        context.current = LexerContext.EndChar
      }

      if (!isEndOfText) {
        context.prev = context.buffer.lift(context.cursor - 1).getOrElse(LexerContext.StartChar)
        context.next = context.buffer.lift(context.cursor + 1).getOrElse(LexerContext.EndChar)
        context.current = context.buffer.lift(context.cursor).getOrElse(LexerContext.EndChar)
        context.cursor = context.cursor + 1
      }
    }

    /**
     * Get a list of the remaining unread charactes in the context
     *
     * @return a list of characters
     */
    def remainingCharacters: List[Char] = {
      context.buffer
        .slice(context.cursor - 1, context.buffer.length)
    }

    /**
     * Check if the context has reached the end of it's input
     *
     * @return true if the current context character is the end character
     */
    private def isEndOfText: Boolean = {
      context.current == LexerContext.EndChar
    }

    /**
     * Check if the a string is still being read from the current context
     *
     * @return true if the what is currently being read is still a string
     */
    private def isStillString: Boolean = {
      val remaining = remainingCharacters.mkString
      val matched = LexerMechanism.TagStartRegex.findAllMatchIn(remaining)

      val matches = matched.nonEmpty

      context.current != '<' || (remainingCharacters.mkString match {
        case LexerMechanism.TagEndRegex(_, _, _) => false
        case LexerMechanism.TagStartRegex(_, _, _) => false
        case _ => true
      })
    }
  }

  private object LexerMechanism {
    val TagStartRegex = "^(<([A-Za-z0-9_-]*)>)(.|\\n)*".r
    val TagEndRegex = "^(</([A-Za-z0-9_-]*)>)(.|\\n)*".r
  }

  /**
   * Provides context to the lexer mechanism so it can keep track of the progress it has made
   *
   * @param buffer The input to tokenise
   * @param captureBuffer The buffer to store data in to retrieve token lexemes
   * @param prev The previous character
   * @param current The current character
   * @param next The next character
   * @param cursor The current position in the input
   */
  private class LexerContext(
    val buffer: List[Char],
    val captureBuffer: StringBuilder = new StringBuilder(),
    var prev: Char = LexerContext.StartChar,
    var current: Char = LexerContext.StartChar,
    var next: Char = LexerContext.EndChar,
    var cursor: Int = 0) {

    /**
     * Clear the capture buffer
     */
    def clearCaptureBuffer(): Unit = {
      captureBuffer.clear()
    }

    /**
     * Flush the capture buffer
     *
     * @return the captured content
     */
    def flushCaptureBuffer(): String = {
      val buffer = captureBuffer.toString()

      clearCaptureBuffer()

      buffer
    }
  }

  private object LexerContext {
    val StartChar: Char = '\u0002'
    val EndChar: Char = '\u0003'
  }
}
