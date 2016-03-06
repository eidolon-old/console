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

import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.Input
import eidolon.console.output.Output

/**
 * Command, the base class for all other commands.
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait Command {
  val name: String
  val description: Option[String] = None
  val aliases: List[String] = List()
  val definition: InputDefinition = new InputDefinition()
  val help: Option[String] = None

  /**
   * Execute this command
   *
   * @param input Input passed to the application
   * @param output Output interface for displaying formatted text
   */
  def execute(input: Input, output: Output): Unit
}
