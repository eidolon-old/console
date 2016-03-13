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

package eidolon.console.command

import eidolon.console.dialog.Dialog
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.Input
import eidolon.console.output.Output

/**
 * A command where the execution always returns a status code of 0, regardless of what actually
 * happened. This is useful to help make some code more concise, without having to return a value.
 * This may however lead to some command's success being seen as ambiguous.
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait AmbiguousCommand extends Command[Unit] {
  /**
   * @inheritdoc
   */
  override def execute(input: Input, output: Output, dialog: Dialog): Unit

  /**
   * @inheritdoc
   */
  final override def run(input: Input, output: Output, dialog: Dialog): Int = {
    execute(input, output, dialog)

    0
  }
}

/**
 * A command where the execution always returns what happened, i.e. a return status code is given,
 * that clearly indicates whether the command succeeded or not (i.e. 0 is success, anything else is
 * failure). The result is unambiguous, and not up for interpretation.
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait UnambiguousCommand extends Command[Int] {
  /**
   * @inheritdoc
   */
  override def execute(input: Input, output: Output, dialog: Dialog): Int

  /**
   * @inheritdoc
   */
  final override def run(input: Input, output: Output, dialog: Dialog): Int = {
    execute(input, output, dialog)
  }
}

/**
 * Command, the base trait for all other commands.
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
sealed trait Command[T] {
  val name: String
  val description: Option[String] = None
  val aliases: List[String] = List()
  val definition: InputDefinition = new InputDefinition()
  val help: Option[String] = None

  /**
   * Execute this command
   *
   * @param input Input for the command to use
   * @param output Output for the command to use
   * @param dialog Dialog for commmand to use
   * @return some indication of success (probably)
   */
  def execute(input: Input, output: Output, dialog: Dialog): T

  /**
   * Run a command, always returning an exit code
   *
   * @param input Input for the command to use
   * @param output Output for the command to use
   * @param dialog Dialog for commmand to use
   * @return an exit code
   */
  def run(input: Input, output: Output, dialog: Dialog): Int
}
