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

package eidolon.console.input.parser

import eidolon.console.input._
import eidolon.console.input.validation._

/**
 * Args Input Parser
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
final class ArgsInputParser(
    private val args: Array[String],
    private val definition: InputDefinition)
  extends InputParser {

  type ParsedInput = Either[InvalidParameter, ValidParameter]

  override def parse(): Either[List[InvalidParameter], Input] = {
    val parsedInputArgs = parseInputArgs(args)
    val missingArgs = findMissingArgs(parsedInputArgs)

    val missingRequired = missingArgs.filter(a => a.isRequired).map(a => new InvalidArgument(a.name))
    val missingDefaults = missingArgs.filter(a => a.hasDefault).map(a => new ValidArgument(a.name, a.default.get))

    val result = new ArgsInputParserAggregate(
      parsedInputArgs.invalid ++: missingRequired,
      parsedInputArgs.valid ++: missingDefaults
    )

    if (result.invalid.nonEmpty) {
      Left(result.invalid)
    } else {
      Right(new Input(
        result.validArguments.map(argument => argument.name -> argument.value).toMap,
        result.validOptions.map(option => option.name -> option.value).toMap
      ))
    }
  }

  private def parseInputArgs(args: Array[String]): ArgsInputParserAggregate = {
    args
      .takeWhile(_ != "--")
      .foldLeft(new ArgsInputParserAggregate())((aggregate, arg) => {
        val result = arg match {
          case token if token.startsWith("--") =>
            parseLongOption(aggregate, token)
          case token if token.startsWith("-") && token != "-" =>
            parseShortOption(aggregate, token)
          case token =>
            parseArgument(aggregate, token)
        }

        result match {
          case Right(parameter) =>
            new ArgsInputParserAggregate(aggregate.invalid, aggregate.valid :+ parameter)
          case Left(error) =>
            new ArgsInputParserAggregate(aggregate.invalid :+ error, aggregate.valid)
        }
      })
  }

  private def findMissingArgs(parsedArgs: ArgsInputParserAggregate): List[InputArgument] = {
    definition.arguments.values
      .filter((argument) => { !parsedArgs.argumentNames.contains(argument.name) })
      .toList
  }

  private def parseArgument(aggregate: ArgsInputParserAggregate, token: String): ParsedInput = {
    definition.getArgument(aggregate.argumentCount) match {
      case Some(argument) => Right(new ValidArgument(argument.name, token))
      case _ => Left(new InvalidArgument(token))
    }
  }

  private def parseShortOption(aggregate: ArgsInputParserAggregate, token: String): ParsedInput = {
    definition.getOptionByShortName(token.drop(1)) match {
      case Some(option) => Right(new ValidOption(option.name))
      case _ => Left(new InvalidOption(token))
    }
  }

  private def parseLongOption(aggregate: ArgsInputParserAggregate, token: String): ParsedInput = {
    definition.getOption(token.drop(2)) match {
      case Some(option) => Right(new ValidOption(option.name))
      case _ => Left(new InvalidOption(token))
    }
  }

  /**
   * Args input parser aggregate object to contain the current parse state
   *
   * @param invalid Invalid parameters
   * @param valid Valid parameters
   */
  private class ArgsInputParserAggregate(
    val invalid: List[InvalidParameter] = List(),
    val valid: List[ValidParameter] = List()) {

    lazy val invalidArguments = invalid.filter(_.isInstanceOf[InvalidArgument]).asInstanceOf[List[InvalidArgument]]
    lazy val invalidOptions = invalid.filter(_.isInstanceOf[InvalidOption]).asInstanceOf[List[InvalidOption]]
    lazy val validArguments = valid.filter(_.isInstanceOf[ValidArgument]).asInstanceOf[List[ValidArgument]]
    lazy val validOptions = valid.filter(_.isInstanceOf[ValidOption]).asInstanceOf[List[ValidOption]]

    lazy val argumentCount = invalidArguments.size + validArguments.size
    lazy val optionCount = invalidOptions.size + validOptions.size

    lazy val invalidCount = invalidArguments.size + invalidOptions.size
    lazy val validCount = validArguments.size + validOptions.size

    lazy val argumentNames = validArguments.map(_.name) ++: invalidArguments.map(_.token)
    lazy val optionNames = validOptions.map(_.name) ++: invalidArguments.map(_.token)
  }
}
