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

package eidolon.console.output.formatter

import eidolon.console.output.Output
import eidolon.console.output.formatter.exception.StyleNotFoundException
import eidolon.console.output.formatter.style._
import scala.xml.{Elem, Text, XML}

/**
 * Console Output Formatter
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 * @param styles A map of output format styles
 */
case class ConsoleOutputFormatter(
    override val styles: OutputFormatterStyleGroup)
  extends OutputFormatter {

  /**
   * @inheritdoc
   */
  override def format(message: String, mode: Int = Output.ModeNormal): String = {
    // @todo: Handle exceptions from XML parsing for things like missing styles
    mode match {
      case Output.ModeRaw => message
      case _ => renderString(message, mode)
    }
  }

  /**
   * @inheritdoc
   */
  override def withStyle(style: OutputFormatterStyle): OutputFormatter = {
    copy(styles.withStyle(style))
  }

  /**
   * @inheritdoc
   */
  override def withStyles(styles: List[OutputFormatterStyle]): OutputFormatter = {
    copy(this.styles.withStyles(styles))
  }

  /**
   * Render a string with styles with the given mode
   *
   * @param message The message to render
   * @param mode The mode to render with
   * @return The rendered string
   */
  private def renderString(message: String, mode: Int): String = {
    val xml = XML.loadString("<root>" + message + "</root>")

    renderNode(xml, mode)
  }

  /**
   * Render an XML node with styles with the given mode
   *
   * @param element The node to render
   * @param mode The mode to render with
   * @return The rendered string
   */
  private def renderNode(element: Elem, mode: Int): String = {
    element.child.foldLeft("")((aggregate, child) => {
      val result = child match {
        case text: Text => text.text
        case node: Elem if mode == Output.ModeNormal => applyStyle(node.label, renderNode(node, mode))
        case node: Elem if mode == Output.ModePlain => renderNode(node, mode)
      }

      aggregate + result
    })
  }

  /**
   * Apply a style from the styles available in this formatter
   *
   * @param styleName The name of the style to apply
   * @param message The message to apply the style to
   * @return The styled message
   */
  private def applyStyle(styleName: String, message: String): String = {
    try {
      styles.styles.find(_.name == styleName).get.applyStyle(message)
    } catch {
      case e: NoSuchElementException => throw new StyleNotFoundException(styleName)
    }
  }
}
