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
import eidolon.console.input.Input
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.definition.parameter.InputArgument
import eidolon.console.output.Output

/**
 * ListCommand
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 *
 * @param application An application
 * @param descriptor A console descriptor
 */
class ListCommand(
    application: Application,
    descriptor: Descriptor)
  extends Command {

  override val name = "list"
  override val description = Some("Lists commands")
  override val definition = new InputDefinition()
    .withArgument(
      "namespace",
      InputArgument.OPTIONAL,
      Some("A command's namespace to list commands under")
    )

  /**
   * @inheritdoc
   */
  override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
    output.writeln(application.logo)
    output.writeln(s"<info>${application.name}</info> version <comment>${application.version}</comment>")
    output.writeln("")

    descriptor.describeApplication(output, application)
  }
}
