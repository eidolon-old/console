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
import eidolon.console.output.Output

/**
 * TestCommand
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class TestCommand extends Command {
  override val name = "test:test"
  override val description = Some("Just a test command to test the command listing")

  override def execute(input: Input, output: Output): Unit = {

  }
}
