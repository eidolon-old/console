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

import eidolon.console.Application
import eidolon.console.command.Command
import eidolon.console.input.definition.{InputArgument, InputDefinition}


/**
 * Main
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object Main extends App {
  val definition = new InputDefinition()
    .withArgument(new InputArgument("source"))
    .withArgument(new InputArgument("destination", default = Some(".")))

  val installCommand = Command("install")
    .withAlias("i")
    .withAlias("ins")
    .withDefinition(definition);

  // Elsewhere...
  val application = Application()
    .withCommand(installCommand)

  println(application)
}
