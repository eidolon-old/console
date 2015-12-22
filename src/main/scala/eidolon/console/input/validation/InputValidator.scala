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

package eidolon.console.input.validation

import eidolon.console.input.definition._
import eidolon.console.input.parser._

/**
 * Input Validator
 *
 * Validates the parsed input produced by an input parser with a given input definition, checking
 * for missing required arguments, duplicates, and filling in missing optional parameters that have
 * default values. In other words, produces a valid final list of input arguments.
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputValidator {
  def validate(definition: InputDefinition, parsedInput: List[ParsedInputParameter]): InputValidatorResult = {
    val validatedParsedInput = validateParsedInput(definition, parsedInput)

    val missingArguments = findMissingArguments(definition, validatedParsedInput)
    val missingRequired = missingArguments.filter(a => a.isRequired).map(a => new InvalidArgument(a.name))
    val missingDefaults = missingArguments.filter(a => a.hasDefault).map(a => new ValidArgument(a.name, a.default.get))

    new InputValidatorResult(
      validatedParsedInput.invalid ++: missingRequired,
      validatedParsedInput.valid ++: missingDefaults
    )
  }

  private def validateParsedInput(
      definition: InputDefinition,
      parsedInput: List[ParsedInputParameter]): InputValidatorResult = {

    parsedInput
      .foldLeft(new InputValidatorResult())((aggregate, param) => {
        val result = param match {
          case argument if argument.isInstanceOf[ParsedInputArgument] =>
            validateArgument(definition, aggregate, argument.asInstanceOf[ParsedInputArgument])
          case option if option.isInstanceOf[ParsedInputLongOption] =>
            validateLongOption(definition, aggregate, option.asInstanceOf[ParsedInputLongOption])
          case option if option.isInstanceOf[ParsedInputShortOption] =>
            validateShortOption(definition, aggregate, option.asInstanceOf[ParsedInputShortOption])
        }

        result match {
          case Right(parameter) =>
            new InputValidatorResult(aggregate.invalid, aggregate.valid :+ parameter)
          case Left(error) =>
            new InputValidatorResult(aggregate.invalid :+ error, aggregate.valid)
        }
      })
  }

  private def validateArgument(
      definition: InputDefinition,
      aggregate: InputValidatorResult,
      parsedArgument: ParsedInputArgument): Either[InvalidParameter, ValidParameter] = {

    definition.getArgument(aggregate.argumentCount) match {
      case Some(argument) => Right(new ValidArgument(argument.name, parsedArgument.token))
      case _ => Left(new InvalidArgument(parsedArgument.token))
    }
  }

  private def validateLongOption(
      definition: InputDefinition,
      aggregate: InputValidatorResult,
      parsedOption: ParsedInputLongOption): Either[InvalidParameter, ValidParameter] = {

    def validateOptionMode(option: InputOption): Either[InvalidParameter, ValidParameter] = {
      option.mode match {
        // Not expecting value, ignore any given
        case InputOption.VALUE_NONE => {
          Right(new ValidOption(option.name, None))
        }

        // Optional value, if one is set use it, otherwise use default, otherwise None
        case InputOption.VALUE_OPTIONAL => {
          if (parsedOption.value.nonEmpty) {
            Right(new ValidOption(option.name, parsedOption.value))
          } else if (option.defaultValue.nonEmpty) {
            Right(new ValidOption(option.name, option.defaultValue))
          } else {
            Right(new ValidOption(option.name, None))
          }
        }

        // Required value, one must be specified, no defaults
        case InputOption.VALUE_REQUIRED => {
          if (parsedOption.value.nonEmpty) {
            Right(new ValidOption(option.name, parsedOption.value))
          } else {
            Left(new InvalidOption(parsedOption.token))
          }
        }

        case _ => {
          throw new RuntimeException("Unexpected InputOption mode.")
        }
      }
    }

    definition.getOption(parsedOption.token) match {
      case Some(option) => validateOptionMode(option)
      case _ => Left(new InvalidOption(parsedOption.token))
    }
  }

  private def validateShortOption(
      definition: InputDefinition,
      aggregate: InputValidatorResult,
      parsedOption: ParsedInputShortOption): Either[InvalidParameter, ValidParameter] = {

    definition.getOptionByShortName(parsedOption.token) match {
      case Some(option) =>
        validateLongOption(definition, aggregate, new ParsedInputLongOption(option.name, parsedOption.value))
      case _ => Left(new InvalidOption(parsedOption.token))
    }
  }

  private def findMissingArguments(
      definition: InputDefinition,
      parsedArgs: InputValidatorResult): List[InputArgument] = {

    definition.arguments.values
      .filter((argument) => { !parsedArgs.argumentNames.contains(argument.name) })
      .toList
  }
}
