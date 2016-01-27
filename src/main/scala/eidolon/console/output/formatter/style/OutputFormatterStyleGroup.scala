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

package eidolon.console.output.formatter.style

/**
 * Output Formatter Style Group
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class OutputFormatterStyleGroup(val styles: Map[String, OutputFormatterStyle] = Map()) {
  /**
   * Create a copy of this output formatter style group with the given style
   *
   * @param style A style to add
   * @return a copy of the output formatter style group
   */
  def withStyle(style: OutputFormatterStyle): OutputFormatterStyleGroup = {
    copy(styles + (style.name -> style))
  }

  /**
   * Create a copy of this output formatter style group with the given styles
   *
   * @param styles A map of styles to add
   * @return a copy of the output formatter style group
   */
  def withStyles(styles: Map[String, OutputFormatterStyle]): OutputFormatterStyleGroup = {
    copy(this.styles ++ styles)
  }
}
