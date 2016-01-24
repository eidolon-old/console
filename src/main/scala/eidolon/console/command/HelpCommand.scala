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
 * HelpCommand
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class HelpCommand extends Command {
  override val name = "help"

  override def execute(input: Input, output: Output): Unit = {
    output.write("Looks like you need some help...")
  }
}
