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
 *    val app = new Application("myapp", "0.1.0-SNAPSHOT")
 *      .withCommand(new ExampleCommand())
 *      .run()
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class Application(
    val name: String,
    val version: String,
    val commands: Map[String, Command[_]] = Map()) {

  def withCommand[T <: Command[_]](command: T): Application = {
    copy(commands ++ command.aliases.map(_ -> command) + (command.name -> command))
  }

  private def copy(commands: Map[String, Command[_]]): Application = {
    new Application(name, version, commands = commands)
  }
}

object Application {
  def apply(name: String, version: String): Application = {
    new Application(name, version)
  }
}