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

/**
 * ConsoleDialog
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleDialog extends Dialog {
  val console = System.console()

  override def ask(
      output: Output,
      question: String,
      default: Option[String],
      mode: Int = Output.OutputNormal)
    : String = {

    output.write(question + " ", mode = mode)

    if (default.nonEmpty) {
      output.write(s"[${default.get}] ")
    }

    console.readLine()
  }

  override def askConfirmation(
      output: Output,
      question: String,
      default: Boolean,
      mode: Int = Output.OutputNormal)
    : Boolean = {

    output.write(question + " ", mode = mode)

    if (default) {
      output.write(s"[Yn] ")
    } else {
      output.write(s"[yN] ")
    }

    val result = console.readLine()

    if (default) {
      result == "y" || result == ""
    } else {
      result == "y"
    }
  }

  override def askSensitive(
      output: Output,
      question: String,
      mode: Int = Output.OutputNormal)
    : String = {

    output.write(question + " ", mode = mode)

    console.readPassword().mkString
  }
}
