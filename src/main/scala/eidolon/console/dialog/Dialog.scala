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
  def ask(
      output: Output,
      question: String,
      default: Option[String] = None,
      mode: Int = Output.OutputNormal)
    : String

  def askConfirmation(
      output: Output,
      question: String,
      default: Boolean = false,
      mode: Int = Output.OutputNormal)
    : Boolean

  def askSensitive(
      output: Output,
      question: String,
      mode: Int = Output.OutputNormal)
    : String
}
