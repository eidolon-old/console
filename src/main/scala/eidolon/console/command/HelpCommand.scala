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

import eidolon.console.Application
import eidolon.console.descriptor.Descriptor
import eidolon.console.dialog.Dialog
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.Input
import eidolon.console.input.definition.parameter.InputArgument
import eidolon.console.output.Output

/**
 * HelpCommand
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class HelpCommand(
    application: Application,
    descriptor: Descriptor)
  extends Command {

  override val name = "help"
  override val description = Some("Displays help for a command")
  override val definition = new InputDefinition()
    .withArgument(
      "command_name",
      InputArgument.OPTIONAL,
      Some("The command name"),
      Some("help")
    )

  /**
   * @inheritdoc
   */
  override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
    val commandName = input.arguments.getOrElse("command_name", "")
    val commandOpt = application.commands.get(commandName)

    output.writeln(application.logo)
    output.writeln(s"<info>${application.name}</info> version <comment>${application.version}</comment>")
    output.writeln("")

    if (commandOpt.nonEmpty) {
      val command = commandOpt.get

      descriptor.describeCommand(output, application, command)
    } else {
      output.writeln("<error>Command '%s' does not exist.</error>".format(commandName))
      output.writeln("");
    }
  }
}
