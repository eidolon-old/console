/**
 * This file is part of the "console" project.
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
  def tokenise(input: String): List[Token] = {
    val context = new LexerContext(input.toCharArray.toList)
    val mechanism = new LexerMechanism(context)

    mechanism.tokenise()
  }

  private class LexerMechanism(val context: LexerContext) {
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

    def readNextToken(): Token = {
      remainingCharacters.mkString match {
        case LexerMechanism.TagEndRegex(_, _, _) => readStyleCloseToken()
        case LexerMechanism.TagStartRegex(_, _, _) => readStyleOpenToken()
        case _ => readStringToken()
      }
    }

    def readStyleOpenToken(): StyleOpenToken = {
      read()

      while (!isEndOfText && context.current != '>') {
        context.captureBuffer.append(context.current)
        read()
      }

      read()

      new StyleOpenToken(context.flushCaptureBuffer())
    }

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

    def readStringToken(): StringToken = {
      def buildNextChar(): Unit = {
        context.captureBuffer.append(context.current)
        read()

        if (!isEndOfText) {
          buildStringTokenValue()
        }
      }

      def buildStringTokenValue(): Unit = {
        if (context.current == '<') {
          remainingCharacters.mkString match {
            case LexerMechanism.TagEndRegex(_, _, _) =>
            case LexerMechanism.TagStartRegex(_, _, _) =>
            case _ => buildNextChar()
          }
        } else {
          buildNextChar()
        }
      }

      buildStringTokenValue()

      new StringToken(context.flushCaptureBuffer())
    }

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

    def remainingCharacters: List[Char] = {
      context.buffer
        .indices
        .filter(_ >= (context.cursor - 1))
        .map(context.buffer.lift(_).get)
        .toList
    }

    private def isEndOfText: Boolean = {
      context.current == LexerContext.EndChar
    }
  }

  private object LexerMechanism {
    val TagStartRegex = "^(<([A-Za-z0-9_-]*)>)(.*)".r
    val TagEndRegex = "^(</([A-Za-z0-9_-]*)>)(.*)".r
  }

  private class LexerContext(
    val buffer: List[Char],
    val captureBuffer: StringBuilder = new StringBuilder(),
    var prev: Char = LexerContext.StartChar,
    var current: Char = LexerContext.StartChar,
    var next: Char = LexerContext.EndChar,
    var cursor: Int = 0) {

    def clearCaptureBuffer() {
      captureBuffer.clear()
    }

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
