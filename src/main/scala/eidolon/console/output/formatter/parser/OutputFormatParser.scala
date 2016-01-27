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

package eidolon.console.output.formatter.parser

import eidolon.console.output.formatter.lexer.token.{StyleCloseToken, StyleOpenToken, StringToken, Token}
import eidolon.console.output.formatter.tree._

/**
 * OutputFormatParser
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class OutputFormatParser {
  /**
   * Parse the given list of tokens into a tree of nodes
   *
   * @param tokens A list of tokens
   * @return a parsed tree of nodes
   */
  def parse(tokens: List[Token]): Node = {
    val mechanism = new ParserMechanism()

    mechanism.buildNode(new RootNode(), tokens)
  }

  /**
   * The parser mechanism, an instance if made for each parse
   */
  private class ParserMechanism {
    var position: Int = -1

    /**
     * Build on a given node with the given tokens
     *
     * @param node A node to build on
     * @param tokens Tokens to build onto the node
     * @return a built node
     */
    def buildNode(node: Node, tokens: List[Token]): Node = {
      position = position + 1

      node match {
        case RootNode(children) => buildRootNode(node, tokens, children)
        case StyleNode(children, style) => buildStyleNode(node, tokens, children, style)
      }
    }

    /**
     * Build onto a root node
     *
     * @param node A root node
     * @param tokens Tokens to build onto the node
     * @param children The node's children
     * @return a built node
     */
    private def buildRootNode(
        node: Node,
        tokens: List[Token],
        children: List[Node])
      : Node = {

      tokens.lift(position) match {
        case Some(StringToken(string)) =>
          // If we hit a string token, just add it to the current node
          buildNode(new RootNode(children :+ new StringNode(string)), tokens)

        case Some(StyleOpenToken(style)) =>
          // If we hit a style token, build a child node
          buildNode(
            new RootNode(
              children :+ buildNode(
                new StyleNode(List(), style),
                tokens
              )
            ),
            tokens
          )

        case Some(StyleCloseToken(style)) => node
        case None => node
        case _ =>
          throw new UnsupportedOperationException(
            "Cannot build unknown node of type '%s'".format(node.getClass)
          )
      }
    }

    /**
     * Built onto a style node
     *
     * @param node A style node
     * @param tokens Tokens to build onto the node
     * @param children The node's children
     * @param style The style of the style node
     * @return a built node
     */
    private def buildStyleNode(
        node: Node,
        tokens: List[Token],
        children: List[Node],
        style: String)
      : Node = {

      tokens.lift(position) match {
        case Some(StringToken(string)) =>
          buildNode(new StyleNode(children :+ new StringNode(string), style), tokens)

        case Some(StyleOpenToken(childStyle)) =>
          // If we hit a style token, build a child node
          buildNode(
            new StyleNode(
              children :+ buildNode(
                new StyleNode(List(), childStyle),
                tokens
              ),
              style
            ),
            tokens
          )

        case Some(StyleCloseToken(childStyle)) => node
        case None => node
        case _ =>
          throw new UnsupportedOperationException(
            "Cannot build unknown node of type '%s'".format(node.getClass)
          )
      }
    }
  }
}
