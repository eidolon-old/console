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
import eidolon.console.descriptor.CommandDescriptor
import eidolon.console.dialog.Dialog
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.Input
import eidolon.console.input.definition.parameter.InputArgument
import eidolon.console.output.Output

/**
 * Help Command
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 * @param application An application
 * @param descriptor A console descriptor
 */
class HelpCommand(
    application: Application,
    descriptor: CommandDescriptor)
  extends UnambiguousCommand {

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
  override def execute(input: Input, output: Output, dialog: Dialog): Int = {
    val commandName = input.arguments.getOrElse("command_name", "")
    val commandOpt = application.commands.find(command => {
      command.name == commandName || command.aliases.contains(commandName)
    })

    output.writeln(application.logo)
    output.writeln(s"<info>${application.name}</info> version <comment>${application.version}</comment>")
    output.writeln("")

    output.writeln("Hello err")

    if (commandOpt.nonEmpty) {
      output.write(descriptor.describe(application, application.definition, commandOpt.get))

      0
    } else {
      output.writeln("<error>Command '%s' does not exist.</error>".format(commandName))
      output.writeln("")

      1
    }
  }
}
