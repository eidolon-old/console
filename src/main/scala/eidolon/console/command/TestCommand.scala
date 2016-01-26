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
import eidolon.console.input.Input
import eidolon.console.output.Output

/**
 * TestCommand
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class TestCommand extends Command {
  override val name = "test:test"
  override val description = Some("Just a test command to test the command listing")

  override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
    val askAge = dialog.askConfirmation(output, "<question>May I ask your age?</question>", default = true)

    val age = if (askAge) {
      Some(dialog.ask(output, "<question>How old are you?</question>", Some("23")))
    } else {
      None
    }

    val secret = dialog.askSensitive(output, "<question>Tell me a secret...</question>")

    if (askAge && age.nonEmpty) {
      output.writeln(s"<info>You said your age is ${age.get}</info>")
    }

    output.writeln(s"<info>Your secret was: '$secret'</info>")
  }
}
