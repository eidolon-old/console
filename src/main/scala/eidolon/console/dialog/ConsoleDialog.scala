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

package eidolon.console.dialog

import eidolon.console.output.Output
import eidolon.console.output.writer.OutputWriter

/**
 * Console Dialog
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleDialog extends Dialog {
  private val console = System.console()

  /**
   * @inheritdoc
   */
  override def ask(
      output: Output,
      question: String,
      default: Option[String],
      mode: Int = OutputWriter.ModeNormal)
    : String = {

    output.out.write(question + " ", mode = mode)

    if (default.nonEmpty) {
      output.out.write(s"[<info>${default.get}</info>] ")
    }

    console.readLine()
  }

  /**
   * @inheritdoc
   */
  override def askConfirmation(
      output: Output,
      question: String,
      default: Boolean,
      mode: Int = OutputWriter.ModeNormal)
    : Boolean = {

    output.out.write(question + " ", mode = mode)

    if (default) {
      output.out.write("[<info>Yn</info>] ")
    } else {
      output.out.write("[<info>yN</info>] ")
    }

    val result = console.readLine()

    if (default) {
      result == "y" || result == ""
    } else {
      result == "y"
    }
  }

  /**
   * @inheritdoc
   */
  override def askSensitive(
      output: Output,
      question: String,
      mode: Int = OutputWriter.ModeNormal)
    : String = {

    output.out.write(question + " ", mode = mode)

    console.readPassword().mkString
  }
}
