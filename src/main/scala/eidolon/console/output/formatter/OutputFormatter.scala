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

import eidolon.console.output.formatter.style._
import eidolon.console.output.Output

/**
 * Output Formatter
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait OutputFormatter {
  val styles: OutputFormatterStyleGroup

  /**
   * Format the given message, with the given output setting
   *
   * @param message The message to format
   * @param mode The output mode
   * @return a formatted message
   */
  def format(message: String, mode: Int = Output.OutputNormal): String

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
