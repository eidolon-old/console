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

package eidolon.console.input

/**
 * Input Definition
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
final case class InputDefinition(
    private val arguments: Map[String, InputArgument] = Map(),
    private val options: Map[String, InputOption] = Map()) {

  private val shortOptions = getShortOptionsFromOptions(options)

  def getArgument(name: String): Option[InputArgument] = {
    arguments.get(name)
  }

  def hasArgument(name: String): Boolean = {
    arguments.contains(name)
  }

  def getOption(name: String): Option[InputOption] = {
    options.get(name)
  }

  def getOptionByShortName(shortName: String): Option[InputOption] = {
    for {
      name <- shortOptions.get(shortName)
      option <- getOption(name)
    } yield option
  }

  def hasOption(name: String): Boolean = {
    options.contains(name)
  }

  def withArgument(argument: InputArgument): InputDefinition = {
    new InputDefinition(arguments + (argument.name -> argument), options)
  }

  def withOption(option: InputOption): InputDefinition = {
    new InputDefinition(arguments, options + (option.name -> option))
  }

  private def getShortOptionsFromOptions(input: Map[String, InputOption]): Map[String, String] = {
    input
      .filter({ case (_, option) => option.shortName.nonEmpty })
      .map({ case (_, option) => option.shortName.get -> option.name })
  }
}
