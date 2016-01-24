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

import eidolon.console.input.Input
import eidolon.console.input.definition.{InputArgument, InputDefinition}
import eidolon.console.output.Output

/**
 * Install Command
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class CloneCommand extends Command {
  override val name = "clone"
  override val description = Some("Clone a given template repo to a local destination")
  override val aliases = List("cl")
  override val definition = new InputDefinition()
    .withArgument(new InputArgument(
      "source",
      InputArgument.REQUIRED
    ))
    .withArgument(new InputArgument(
      "destination",
      InputArgument.OPTIONAL
    ))

  override def execute(input: Input, output: Output): Unit = {
    output.write("<info>" + input.toString + "</info>")
  }
}
