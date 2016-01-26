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

package eidolon.console.output.formatter

import eidolon.chroma.Chroma
import eidolon.console.output.formatter.lexer.OutputFormatLexer
import eidolon.console.output.formatter.parser.OutputFormatParser
import eidolon.console.output.formatter.style._
import eidolon.console.output.Output

/**
 * ConsoleOutputFormatter
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class ConsoleOutputFormatter(
    private val chroma: Chroma,
    override val lexer: OutputFormatLexer,
    override val parser: OutputFormatParser,
    override val styles: Map[String, OutputFormatterStyle] = Map())
  extends OutputFormatter {

  val styleGroup = new OutputFormatterStyleGroup()
    .withStyle(new InfoOutputFormatterStyle(chroma))
    .withStyle(new ErrorOutputFormatterStyle(chroma))
    .withStyle(new CommentOutputFormatterStyle(chroma))
    .withStyle(new QuestionOutputFormatterStyle(chroma))
    .withStyles(styles)

  override def format(message: String, mode: Int = Output.OutputNormal): String = {
    doFormat(styleGroup, message, mode)
  }

  override def withStyle(style: OutputFormatterStyle): OutputFormatter = {
    copy(chroma, lexer, parser, styles + (style.name -> style))
  }

  override def withStyles(styles: Map[String, OutputFormatterStyle]): OutputFormatter = {
    copy(chroma, lexer, parser, this.styles ++ styles)
  }
}
