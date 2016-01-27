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

/**
 * Parent Node
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait ParentNode[T <: ParentNode[T]] {
  val children: List[Node]

  /**
   * Create a copy of node with the given child
   *
   * @param child A child node
   * @return a copy of the node with the child
   */
  def withChild(child: T): T
}
