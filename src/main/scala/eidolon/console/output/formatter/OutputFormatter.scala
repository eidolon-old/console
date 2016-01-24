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
 * Formatter
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait OutputFormatter[T <: OutputFormatter[T]] {
  val lexer: OutputFormatLexer = new OutputFormatLexer()
  val parser: OutputFormatParser = new OutputFormatParser()
  val styles: Map[String, OutputFormatterStyle]

  def format(message: String, mode: Int = Output.OutputNormal): String

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

  def hasStyle(name: String): Boolean = {
    styles.contains(name)
  }

  def withStyle(style: OutputFormatterStyle): T
  def withStyles(styles: Map[String, OutputFormatterStyle]): T
}
