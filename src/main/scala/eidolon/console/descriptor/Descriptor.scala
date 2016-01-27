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
  /**
   * Describe the given application, providing information about usage
   *
   * @param output The output to write the description to
   * @param application The application to describe
   */
  def describeApplication(output: Output, application: Application): Unit

  /**
   * Describe the given command, providing information about usage
   *
   * @param output The output to write the description to
   * @param application The application the command belongs to
   * @param command The command to describe
   */
  def describeCommand(output: Output, application: Application, command: Command): Unit

  /**
   * Describe the given input argument
   *
   * @param output The output to write the description to
   * @param definition The definition the input argument belongs to
   * @param argument The input argument to decribe
   */
  def describeInputArgument(output: Output, definition: InputDefinition, argument: InputArgument): Unit

  /**
   * Describe the given input definition
   *
   * @param output The output to write the description to
   * @param definition The input definition to describe
   */
  def describeInputDefinition(output: Output, definition: InputDefinition): Unit

  /**
   * Describe the given input option
   *
   * @param output The output to write the description to
   * @param definition The definition the input option belongs to
   * @param option The input option to describe
   */
  def describeInputOption(output: Output, definition: InputDefinition, option: InputOption): Unit
}
