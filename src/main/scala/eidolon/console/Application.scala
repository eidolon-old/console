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

package eidolon.console

import eidolon.console.command.Command

/**
 * Application
 *
 * The Application class serves as the entry point for a console application. It is what commands
 * are registered within for use.
 *
 * Usage (e.g. in main):
 *
 *    val app = new Application("myapp", "0.1.0-SNAPSHOT");
 *
 *    app.withCommand(new ExampleCommand())
 *    app.run()
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class Application(
    private val commands: Map[String, Command] = Map()) {

  def withCommand(command: Command): Application = {
    copy(commands ++ command.aliases.map(_ -> command) + (command.name -> command))
  }

  private def copy(
      commands: Map[String, Command]): Application = {

    new Application(commands = commands)
  }
}

object Application {
  def apply(): Application = {
    new Application()
  }
}