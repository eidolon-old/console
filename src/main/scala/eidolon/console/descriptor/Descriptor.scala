/**
 * This file is part of the "console" project.
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
import eidolon.console.input.definition.{InputDefinition, InputOption, InputArgument}

/**
 * Descriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait Descriptor {
  def describeInputArgument(argument: InputArgument): String

  def describeInputOption(option: InputOption): String

  def describeInputDefinition(definition: InputDefinition): String

  def describeCommand(command: Command): String

  def describeApplication(application: Application): String
}
