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

package eidolon.console.output.formatter.tree

import eidolon.console.output.formatter.style.OutputFormatterStyleGroup

/**
 * Node
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait Node {
  /**
   * Render this node (usually rendering all child nodes too)
   *
   * @param styleGroup A group of styles
   * @param styled Whether or not to render with styles
   * @return A rendered string
   */
  def render(styleGroup: OutputFormatterStyleGroup, styled: Boolean): String
}
