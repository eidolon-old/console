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
import jline.console.ConsoleReader

/**
 * Console Dialog
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class Dialog(reader: ConsoleReader) {
  /**
   * Ask a question to get some kind of input during command execution
   *
   * @param output The output to write to
   * @param question The question to ask to prompt user input
   * @param default The default value (if any)
   * @param mode The output mode
   * @return The value in response to the question
   */
  def ask(
      output: Output,
      question: String,
      default: Option[String] = None,
      mode: Int = OutputWriter.ModeNormal)
    : String = {

    val questionPrompt = output.out.formatter.format(
      message = "<question>" + question + "</question> ",
      mode = mode
    )

    val prompt = questionPrompt.concat(if (default.nonEmpty) {
      output.out.formatter.format(s"[<info>${default.get}</info>] ", mode = mode)
    } else {
      ""
    })

    reader.readLine(prompt)
  }

  /**
   * Ask a question to get some kind of confirmation during command execution
   *
   * @param output The output to write to
   * @param question The question to ask to prompt user input
   * @param default The default value
   * @param mode The output mode
   * @return The value in response to the question
   */
  def askConfirmation(
      output: Output,
      question: String,
      default: Boolean = false,
      mode: Int = OutputWriter.ModeNormal)
    : Boolean = {

    val questionPrompt = output.out.formatter.format(
      message = "<question>" + question + "</question> ",
      mode = mode
    )

    val prompt = questionPrompt.concat(if (default) {
      output.out.formatter.format("[<info>Yn</info>] ", mode = mode)
    } else {
      output.out.formatter.format("[<info>yN</info>] ", mode = mode)
    })

    val result = reader.readLine(prompt).toLowerCase

    if (default) {
      result == "y" || result == ""
    } else {
      result == "y"
    }
  }

  /**
   * Ask a question to get some kind of sensitive information during command execution. User input
   * is hidden.
   *
   * @param output The output to write to
   * @param question The question to ask to prompt user input
   * @param mode The output mode
   * @return The value in response to the question
   */
  def askSensitive(
      output: Output,
      question: String,
      mode: Int = OutputWriter.ModeNormal)
    : String = {

    reader.readLine(
      output.out.formatter.format("<question>" + question + "</question> "),
      new Character('*')
    )
  }
}
