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
 *
 * @param arguments A list of input arguments
 * @param options A list of input options
 */
case class InputDefinition(
    arguments: List[InputArgument] = List(),
    options: List[InputOption] = List()) {

  private val shortOptions = getShortOptionsFromOptions(options)

  /**
   * Appends an input definition's arguments and options onto this one's
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

  /**
   * Get an input argument with the given name
   *
   * @param name The name of the input argument to get
   * @return an input argument, if it exists
   */
  def getArgument(name: String): Option[InputArgument] = {
    arguments.find(_.name == name)
  }

  /**
   * Get an input argument at the given index
   *
   * @param index The index of the input argument to get
   * @return an input argument, if it exists
   */
  def getArgumentAtIndex(index: Int): Option[InputArgument] = {
    arguments.lift(index)
  }

  /**
   * Check if an input argument exists in this input definition with the given name
   *
   * @param name The name of the input argument to check
   * @return true if the input argument exists
   */
  def hasArgument(name: String): Boolean = {
    getArgument(name).nonEmpty
  }

  /**
   * Check if an input argument exists in this input definition at the given index
   *
   * @param index The index of the input argument to check
   * @return true if the input argument exists
   */
  def hasArgumentAtIndex(index: Int): Boolean = {
    getArgumentAtIndex(index).nonEmpty
  }

  /**
   * Get an input option with the given name
   *
   * @param name The name of the input option to get
   * @return an input option, if it exists
   */
  def getOption(name: String): Option[InputOption] = {
    options.find(_.name == name)
  }

  /**
   * Get an input option with the given short name
   *
   * @param shortName The short name of the input option to get
   * @return an input option, if it exists
   */
  def getOptionByShortName(shortName: String): Option[InputOption] = {
    for {
      name <- shortOptions.get(shortName)
      option <- getOption(name)
    } yield option
  }

  /**
   * Check if an input option exists in this input definition with the given name
   *
   * @param name The name of the input option to check
   * @return true if the input option exists
   */
  def hasOption(name: String): Boolean = {
    getOption(name).nonEmpty
  }

  /**
   * Create a copy of this input definition with the given input argument
   *
   * @param argument The input argument to add to the copy of this input definition
   * @return a new input definition
   */
  def withArgument(argument: InputArgument): InputDefinition = {
    new InputDefinition(arguments :+ argument, options)
  }

  /**
   * Create a copy of this input definition with the given input argument configuration
   *
   * @param name The name of the input argument
   * @param mode The mode of the input argument
   * @param description The description of the input argument
   * @param default The default value of the input argument
   * @return a new input definition
   */
  def withArgument(
      name: String,
      mode: Int = InputArgument.OPTIONAL,
      description: Option[String] = None,
      default: Option[String] = None)
    : InputDefinition = {

    withArgument(new InputArgument(name, mode, description, default))
  }

  /**
   * Create a copy of this input definition with the given input option
   *
   * @param option The input option to add to the copy of this input definition
   * @return a new input definition
   */
  def withOption(option: InputOption): InputDefinition = {
    new InputDefinition(arguments, options :+ option)
  }

  /**
   * Create a copy of this input definition with the given input option configuration
   *
   * @param name The name of the input option
   * @param shortName The short name of the input option
   * @param mode The mode of the input option
   * @param description The description of the input option
   * @param defaultValue The default value of the input option
   * @return a new input definition
   */
  def withOption(
      name: String,
      shortName: Option[String] = None,
      mode: Int = InputOption.VALUE_NONE,
      description: Option[String] = None,
      defaultValue: Option[String] = None)
    : InputDefinition = {

    withOption(new InputOption(name, shortName, mode, description, defaultValue))
  }

  /**
   * Get a map of input option short names to input option names
   *
   * @param input A list of input options to map
   * @return a map of input options short names to input option names
   */
  private def getShortOptionsFromOptions(input: List[InputOption]): Map[String, String] = {
    input
      .filter({ case (option) => option.shortName.nonEmpty })
      .map({ case (option) => option.shortName.get -> option.name })
      .toMap
  }
}
