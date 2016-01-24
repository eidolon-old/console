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

/**
 * ConsoleOutputFormatter
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class ConsoleOutputFormatter(
    private val chroma: Chroma,
    override val decorated: Boolean = true,
    override val styles: Map[String, OutputFormatterStyle] = Map())
  extends OutputFormatter[ConsoleOutputFormatter] {

  private val infoStyle = new InfoOutputFormatterStyle(chroma)
  private val errorStyle = new ErrorOutputFormatterStyle(chroma)

  val defaultStyles = Map(
    infoStyle.name -> infoStyle,
    errorStyle.name -> errorStyle
  )

  override def format(message: String): String = {
    val consoleStyles = defaultStyles ++ styles

    message
  }

  override def withStyle(style: OutputFormatterStyle): ConsoleOutputFormatter = {
    copy(chroma, decorated, styles + (style.name -> style))
  }
}
