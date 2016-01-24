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
 * CommentOutputFormatterStyle
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class CommentOutputFormatterStyle(chroma: Chroma) extends OutputFormatterStyle {
  override val name: String = "comment"

  override def applyStyle(message: String): String = {
    chroma.yellow(message)
  }
}
