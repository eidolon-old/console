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

import eidolon.chroma.Chroma

/**
 * InfoOutputFormatterStyle
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InfoOutputFormatterStyle(chroma: Chroma) extends OutputFormatterStyle {
  override val name: String = "info"

  override def applyStyle(message: String): String = {
    chroma.green(message)
  }
}
