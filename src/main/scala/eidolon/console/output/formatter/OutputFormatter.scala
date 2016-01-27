/**
 * This file is part of the "Default (Template) Project" project.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the LICENSE is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package eidolon.console.output.formatter

import eidolon.console.output.formatter.lexer.OutputFormatLexer
import eidolon.console.output.formatter.parser.OutputFormatParser
import eidolon.console.output.formatter.style._
import eidolon.console.output.Output

/**
 * Output Formatter
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait OutputFormatter {
  val lexer: OutputFormatLexer
  val parser: OutputFormatParser
  val styles: Map[String, OutputFormatterStyle]

  /**
   * Format the given message, with the given output setting
   *
   * @param message The message to format
   * @param mode The output mode
   * @return a formatted message
   */
  def format(message: String, mode: Int = Output.OutputNormal): String

  /**
   * Perform the formatting, parsing the style tags and applying their corresponding styles
   *
   * @param styleGroup The style group to pull styles from
   * @param message The message to format
   * @param mode The output mode
   * @return a formatted message
   */
  protected def doFormat(
      styleGroup: OutputFormatterStyleGroup,
      message: String,
      mode: Int)
    : String = {

    if (mode == Output.OutputNormal || mode == Output.OutputPlain) {
      val tokens = lexer.tokenise(message)
      val result = parser.parse(tokens)

      result.render(styleGroup, mode == Output.OutputNormal)
    } else {
      message
    }
  }

  /**
   * Create a copy of this output formatter with the given style
   *
   * @param style A style to add
   * @return a copy of the output formatter
   */
  def withStyle(style: OutputFormatterStyle): OutputFormatter

  /**
   * Create a copy of this output formatter with the given style
   *
   * @param styles A style to add
   * @return a copy of the output formatter
   */
  def withStyles(styles: Map[String, OutputFormatterStyle]): OutputFormatter
}
