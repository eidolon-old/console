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
 * Dialog
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait Dialog {
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
      mode: Int = Output.OutputNormal)
    : String

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
      mode: Int = Output.OutputNormal)
    : Boolean

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
      mode: Int = Output.OutputNormal)
    : String
}
