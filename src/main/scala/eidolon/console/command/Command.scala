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

/**
 * Command, the base class for all other commands.
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
final class Command(
    val name: String,
    val aliases: List[String] = List(),
    val definition: InputDefinition = new InputDefinition()) {

  def withAlias(alias: String): Command = {
    copy(aliases = aliases :+ alias)
  }

  def withAliases(newAliases: List[String]): Command = {
    copy(aliases = aliases ++ newAliases)
  }

  def withDefinition(definition: InputDefinition): Command = {
    copy(definition = definition)
  }

  private def copy(
      name: String = name,
      aliases: List[String] = aliases,
      definition: InputDefinition = definition): Command = {

    new Command(name, aliases, definition)
  }
}

object Command {
  def apply(name: String): Command = {
    new Command(name)
  }
}