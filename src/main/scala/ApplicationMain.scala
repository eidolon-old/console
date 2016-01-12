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
import eidolon.console.command.InstallCommand
import eidolon.console.input.definition.{InputArgument, InputDefinition}


/**
 * Main
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object ApplicationMain extends App {
  val installCommand = new InstallCommand()

  // Elsewhere...
  val application = Application("eidolon/console", "0.1.0-SNAPSHOT")
    .withCommand(installCommand)

  println(application)
}
