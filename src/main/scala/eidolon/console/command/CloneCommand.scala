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
import eidolon.console.input.definition.parameter.{InputOption, InputArgument}
import eidolon.console.input.definition.InputDefinition
import eidolon.console.output.Output

/**
 * Install Command
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class CloneCommand extends Command {
  override val name = "eidola:clone"
  override val description = Some("Clone a given template repo to a local destination")
  override val aliases = List("eidola:cl")
  override val definition = new InputDefinition()
    .withArgument(
      "source",
      InputArgument.REQUIRED,
      Some("The source repository to clone from")
    )
    .withArgument(
      "destination",
      InputArgument.OPTIONAL,
      Some("The destination directory to clone into")
    )
    .withOption(
      "no-cache",
      mode = InputOption.VALUE_NONE,
      description = Some("Set to disable using locally cached copies of templates")
    )
    .withOption(
      "cache-dir",
      Some("c"),
      mode = InputOption.VALUE_OPTIONAL,
      description = Some("Path to local cache directory to use"),
      defaultValue = Some("./.eidola-cache")
    )

  override val help = Some(helpText)

  /**
   * @inheritdoc
   */
  override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
    output.writeln("<question>Is this thing on?</question>")
    output.writeln("<comment>Looks like it is...</comment>")
    output.writeln("<info>Cloning some stuff...</info>")
  }

  /**
   * Get this command's help text
   *
   * @return The help text
   */
  private def helpText: String = {
    """The <info>clone</info> command creates a clone of a remote repository
      |template and asks you questions to fill in template values.
      |
      |<info>$ eidola clone example/repo:gh .</info>""".stripMargin
  }
}
