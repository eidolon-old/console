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
import eidolon.console.descriptor.{TextDescriptor, Descriptor}
import eidolon.console.input.definition.{InputArgument, InputDefinition}
import eidolon.console.input.Input
import eidolon.console.output.Output

/**
 * HelpCommand
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class HelpCommand(
    application: Application,
    descriptor: Descriptor = new TextDescriptor())
  extends Command {

  override val name = "help"
  override val definition = new InputDefinition()
    .withArgument(new InputArgument(
      "command_name",
      InputArgument.OPTIONAL
    ))

  override def execute(input: Input, output: Output): Unit = {
    val commandName = input.arguments.getOrElse("command_name", "")
    val commandOpt = application.commands.get(commandName)

    output.write(application.logo)
    output.writeln(s"<info>${application.name}</info> version <comment>${application.version}</comment>")
    output.writeln("")

    if (commandOpt.nonEmpty) {
      val command = commandOpt.get

      descriptor.describeCommand(output, application, command)
    } else {
      descriptor.describeApplication(output, application)
    }
  }
}
