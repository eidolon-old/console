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

import eidolon.chroma.Chroma
import eidolon.console.output.formatter.{ErrorOutputFormatterStyle, InfoOutputFormatterStyle}

/**
 * StyleNode
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class StyleNode(
    override val children: List[Node] = List(),
    val style: String)
  extends Node
  with ParentNode[StyleNode] {

  override def render(): String = {
    val info = new InfoOutputFormatterStyle(Chroma())
    val error = new ErrorOutputFormatterStyle(Chroma())

    val styles = Map(
      info.name -> info,
      error.name -> error
    )

    val renderer = styles.get(style).get

    renderer.applyStyle(children.foldLeft("")((aggregate, node) => {
      aggregate + node.render()
    }))
  }

  override def withChild(child: StyleNode): StyleNode = {
    copy(children :+ child)
  }
}
