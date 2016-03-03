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
import eidolon.console.input.definition.parameter.{InputOption, InputArgument}
import eidolon.console.input.parser.parameter.{ParsedInputShortOption, ParsedInputParameter, ParsedInputLongOption, ParsedInputArgument}
import eidolon.console.input.validation.parameter._

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
  /**
   * Validate the given parsed input against a given input definition
   *
   * @param definition The input definition to validate against
   * @param parsedInput The parsed input to validate
   * @return an input validator result
   */
  def validate(
      definition: InputDefinition,
      parsedInput: List[ParsedInputParameter])
    : InputValidatorResult = {

    val validatedParsedInput = validateParsedInput(definition, parsedInput)

    val missingArguments = findMissingArguments(definition, validatedParsedInput)
    val missingRequired = missingArguments.filter(a => a.isRequired).map(a => new InvalidArgument(a.name))
    val missingDefaults = missingArguments.filter(a => a.hasDefault).map(a => new ValidArgument(a.name, a.default.get))

    new InputValidatorResult(
      validatedParsedInput.invalid ++: missingRequired,
      validatedParsedInput.valid ++: missingDefaults
    )
  }

  /**
   * Internally validate the given parsed input against a given input definition (not all checks
   * are performed in this method)
   *
   * @param definition The input definition to validate against
   * @param parsedInput The parsed input to validate
   * @return an input validator results
   */
  private def validateParsedInput(
      definition: InputDefinition,
      parsedInput: List[ParsedInputParameter])
    : InputValidatorResult = {

    parsedInput
      .foldLeft(new InputValidatorResult())((aggregate, param) => {
        val result = param match {
          case argument: ParsedInputArgument =>
            validateArgument(definition, aggregate, argument)
          case option: ParsedInputLongOption =>
            validateLongOption(definition, aggregate, option)
          case option: ParsedInputShortOption =>
            validateShortOption(definition, aggregate, option)
        }

        result match {
          case Right(parameter) =>
            new InputValidatorResult(aggregate.invalid, aggregate.valid :+ parameter)
          case Left(error) =>
            new InputValidatorResult(aggregate.invalid :+ error, aggregate.valid)
        }
      })
  }

  /**
   * Validate the given parsed input argument against a given input definition
   *
   * @param definition The input definition to validate against
   * @param aggregate The aggregate input validator result
   * @param parsedArgument The parsed input argument to validate
   * @return an input validator result
   */
  private def validateArgument(
      definition: InputDefinition,
      aggregate: InputValidatorResult,
      parsedArgument: ParsedInputArgument)
    : Either[InvalidParameter, ValidParameter] = {

    definition.getArgument(aggregate.argumentCount) match {
      case Some(argument) => Right(new ValidArgument(argument.name, parsedArgument.token))
      case _ => Left(new InvalidArgument(parsedArgument.token))
    }
  }

  /**
   * Validate the given parsed input long option against a given input definition
   *
   * @param definition The input definition to validate against
   * @param aggregate The aggregate input validator result
   * @param parsedOption The parsed input long option to validate
   * @return an input validator result
   */
  @throws[RuntimeException]("If an unexpected InputOption mode is encountered")
  private def validateLongOption(
      definition: InputDefinition,
      aggregate: InputValidatorResult,
      parsedOption: ParsedInputLongOption)
    : Either[InvalidParameter, ValidParameter] = {

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

  /**
   * Validate the given parsed input short option against a given input definition
   *
   * @param definition The input definition to validate against
   * @param aggregate The aggregate input validator result
   * @param parsedOption The parsed input short option to validate
   * @return an input validator result
   */
  private def validateShortOption(
      definition: InputDefinition,
      aggregate: InputValidatorResult,
      parsedOption: ParsedInputShortOption)
    : Either[InvalidParameter, ValidParameter] = {

    definition.getOptionByShortName(parsedOption.token) match {
      case Some(option) =>
        validateLongOption(definition, aggregate, new ParsedInputLongOption(option.name, parsedOption.value))
      case _ => Left(new InvalidOption(parsedOption.token))
    }
  }

  /**
   * Find the arguments defined in the given definition that weren't in the given validated input
   *
   * @param definition An input definition to compare against
   * @param parsedArgs A validated input result to check input from
   * @return a list of missing arguments
   */
  private def findMissingArguments(
      definition: InputDefinition,
      parsedArgs: InputValidatorResult)
    : List[InputArgument] = {

    definition.arguments.values
      .filter((argument) => { !parsedArgs.argumentNames.contains(argument.name) })
      .toList
  }
}
