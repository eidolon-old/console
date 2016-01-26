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

package eidolon.console.input.definition

import eidolon.console.input.definition.parameter.{InputOption, InputArgument}

/**
 * Input Definition
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
final case class InputDefinition(
    arguments: Map[String, InputArgument] = Map(),
    options: Map[String, InputOption] = Map()) {

  private val shortOptions = getShortOptionsFromOptions(options)

  /**
   * Appends an InputDefinition's arguments and options onto this InputDefinition
   *
   * @param definition the input definition to append to this one
   * @return a new input definition
   */
  def ++(definition: InputDefinition): InputDefinition = {
    new InputDefinition(
      arguments ++ definition.arguments,
      options ++ definition.options
    )
  }

  def getArgument(index: Int): Option[InputArgument] = {
    arguments.values.toList.lift(index)
  }

  def hasArgument(index: Int): Boolean = {
    arguments.values.toList.lift(index).isDefined
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

  def withArgument(
    name: String,
    mode: Int = InputArgument.OPTIONAL,
    description: Option[String] = None,
    default: Option[String] = None)
  : InputDefinition = {

    withArgument(new InputArgument(name, mode, description, default))
  }

  def withOption(option: InputOption): InputDefinition = {
    new InputDefinition(arguments, options + (option.name -> option))
  }

  def withOption(
      name: String,
      shortName: Option[String] = None,
      mode: Int = InputOption.VALUE_NONE,
      description: Option[String] = None,
      defaultValue: Option[String] = None)
    : InputDefinition = {

    withOption(new InputOption(name, shortName, mode, description, defaultValue))
  }

  private def getShortOptionsFromOptions(input: Map[String, InputOption]): Map[String, String] = {
    input
      .filter({ case (_, option) => option.shortName.nonEmpty })
      .map({ case (_, option) => option.shortName.get -> option.name })
  }
}
