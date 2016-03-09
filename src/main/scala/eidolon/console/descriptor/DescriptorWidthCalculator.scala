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

package eidolon.console.descriptor

import eidolon.console.command.Command
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.definition.parameter.{InputArgument, InputOption}

/**
 * Input Definition Width Calculator
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait DescriptorWidthCalculator {
  /**
   * Calculate the widths of all of the given commands
   *
   * @param commands The commands to calculate the widths of
   * @return A list of command widths
   */
  protected def calculateCommandWidths(commands: List[Command]): List[Int] = {
    commands.map((command) => {
      command.name.length
    })
  }

  /**
   * Calulcate the total width of the synopsi for the given input definition's arguments and options
   *
   * @param definition The definition to calculate the width of
   * @return The width
   */
  protected def calculateDefinitionWidth(definition: InputDefinition): Int = {
    val argumentWidths = calculateArgumentWidths(definition.arguments)
    val optionWidths = calculateOptionWidths(definition.options)

    (argumentWidths ++ optionWidths).reduce((a, b) => {
      if (a >= b) a else b
    })
  }

  /**
   * Calculate the widths of all of the given input arguments
   *
   * @param arguments The input arguments to calculate the widths of
   * @return A list of input argument widths
   */
  private def calculateArgumentWidths(arguments: List[InputArgument]): List[Int] = {
    arguments.map((argument) => {
      argument.name.length
    })
  }

  /**
   * Calculate the widths of all of the given input options
   *
   * @param options The input options to calculate the widths of
   * @return A list of input option widths
   */
  private def calculateOptionWidths(options: List[InputOption]): List[Int] = {
    options.map((option) => {
      // "-" + shortName + ", --" + name
      val nameLength = 1 + math.max(option.shortName.getOrElse("").length, 1) + 4 + option.name.length
      val valueLength = option.acceptsValue match {
        case true if option.isOptionalValue => 3 + option.name.length
        case true if !option.isOptionalValue => 1 + option.name.length
        case false => 0
      }

      nameLength + valueLength
    })
  }
}
