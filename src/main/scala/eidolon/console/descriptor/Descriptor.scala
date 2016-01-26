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

package eidolon.console.descriptor

import eidolon.console.Application
import eidolon.console.command.Command
import eidolon.console.input.definition.parameter.{InputOption, InputArgument}
import eidolon.console.input.definition.InputDefinition
import eidolon.console.output.Output

/**
 * Descriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait Descriptor {
  def describeApplication(output: Output, application: Application): Unit
  def describeCommand(output: Output, application: Application, command: Command): Unit
  def describeInputArgument(output: Output, argument: InputArgument, totalWidth: Int): Unit
  def describeInputDefinition(output: Output, definition: InputDefinition): Unit
  def describeInputOption(output: Output, option: InputOption, totalWidth: Int): Unit
}
