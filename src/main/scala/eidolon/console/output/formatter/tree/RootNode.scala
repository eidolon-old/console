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

package eidolon.console.output.formatter.tree

/**
 * RootNode
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class RootNode(
    override val children: List[Node] = List())
  extends Node
  with ParentNode[RootNode] {

  override def render(): String = {
    children.foldLeft("")((aggregate, node) => {
      aggregate + node.render()
    })
  }

  override def withChild(child: RootNode): RootNode = {
    copy(children :+ child)
  }
}
