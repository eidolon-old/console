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
import eidolon.console.output.formatter.OutputFormatter
import jline.console.ConsoleReader

/**
 * Console Dialog
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class Dialog(formatter: OutputFormatter, reader: ConsoleReader) {
  /**
   * Ask a question to get some kind of input during command execution
   *
   * @param question The question to ask to prompt user input
   * @param default The default value (if any)
   * @param mode The output mode
   * @return The value in response to the question
   */
  def ask(
      question: String,
      default: Option[String] = None,
      mode: Int = Output.ModeNormal)
    : String = {

    val questionPrompt = formatter.format(
      message = "<question>" + question + "</question> ",
      mode = mode
    )

    val prompt = questionPrompt.concat(if (default.nonEmpty) {
      formatter.format(s"[<info>${default.get}</info>] ", mode = mode)
    } else {
      ""
    })

    val result = reader.readLine(prompt)

    if (default.nonEmpty && result.isEmpty) {
      default.get
    } else {
      result
    }
  }

  /**
   * Ask a question to get some kind of confirmation during command execution
   *
   * @param question The question to ask to prompt user input
   * @param default The default value
   * @param mode The output mode
   * @return The value in response to the question
   */
  def askConfirmation(
      question: String,
      default: Boolean = false,
      mode: Int = Output.ModeNormal)
    : Boolean = {

    val questionPrompt = formatter.format(
      message = "<question>" + question + "</question> ",
      mode = mode
    )

    val prompt = questionPrompt.concat(if (default) {
      formatter.format("[<info>Yn</info>] ", mode = mode)
    } else {
      formatter.format("[<info>yN</info>] ", mode = mode)
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
   * @param question The question to ask to prompt user input
   * @param mode The output mode
   * @return The value in response to the question
   */
  def askSensitive(
      question: String,
      default: Option[String] = None,
      mode: Int = Output.ModeNormal)
    : String = {

    val questionPrompt = formatter.format(
      message = "<question>" + question + "</question> ",
      mode = mode
    )

    val prompt = questionPrompt.concat(if (default.nonEmpty) {
      formatter.format(s"[<info>${default.get}</info>] ", mode = mode)
    } else {
      ""
    })

    val result = reader.readLine(
      prompt,
      new Character('*')
    )

    if (default.nonEmpty && result.isEmpty) {
      default.get
    } else {
      result
    }
  }
}
