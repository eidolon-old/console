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

/**
 * Descriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait Descriptor {
  /**
   * Describe the given application, providing information about usage
   *
   * @param application The application to describe
   * @return the describption
   */
  def describeApplication(application: Application): String

  /**
   * Describe the given command, providing information about usage
   *
   * @param application The application the command belongs to
   * @param command The command to describe
   * @return the describption
   */
  def describeCommand(application: Application, command: Command): String

  /**
   * Describe the given input argument
   *
   * @param definition The definition the input argument belongs to
   * @param argument The input argument to decribe
   * @return the describption
   */
  def describeInputArgument(definition: InputDefinition, argument: InputArgument): String

  /**
   * Describe the given input definition
   *
   * @param definition The input definition to describe
   * @return the describption
   */
  def describeInputDefinition(definition: InputDefinition): String

  /**
   * Describe the given input option
   *
   * @param definition The definition the input option belongs to
   * @param option The input option to describe
   * @return the describption
   */
  def describeInputOption(definition: InputDefinition, option: InputOption): String
}
