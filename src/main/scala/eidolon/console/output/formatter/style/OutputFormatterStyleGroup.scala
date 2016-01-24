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

package eidolon.console.output.formatter.style

/**
 * OutputFormatterStyleGroup
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class OutputFormatterStyleGroup(val styles: Map[String, OutputFormatterStyle] = Map()) {
  def withStyle(style: OutputFormatterStyle): OutputFormatterStyleGroup = {
    copy(styles + (style.name -> style))
  }

  def withStyles(styles: Map[String, OutputFormatterStyle]): OutputFormatterStyleGroup = {
    copy(this.styles ++ styles)
  }
}
