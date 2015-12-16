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
  type ParsedInputList = List[ParsedInput]

  override def parse(): InputParserResult = {
    val parsedArgs = args
      .takeWhile(_ != "--")
      .foldLeft(new InputParserResult())((aggregate, arg) => {
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
            new InputParserResult(aggregate.invalid, aggregate.valid :+ parameter)
          case Left(error) =>
            new InputParserResult(aggregate.invalid :+ error, aggregate.valid)
        }
      })

    val missingArgs = definition.arguments.values
      .filter((argument) => { !parsedArgs.argumentNames.contains(argument.name) })
      .map((argument) => { new InvalidArgument(argument.name) })
      .toList

    new InputParserResult(
      parsedArgs.invalid ++: missingArgs,
      parsedArgs.valid
    )
  }

  private def parseArgument(aggregate: InputParserResult, token: String): ParsedInput = {
    definition.getArgument(aggregate.argumentCount) match {
      case Some(argument) => Right(new ValidArgument(argument.name, List(token)))
      case _ => Left(new InvalidArgument(token))
    }
  }

  private def parseShortOption(aggregate: InputParserResult, token: String): ParsedInput = {
    definition.getOptionByShortName(token.drop(1)) match {
      case Some(option) => Right(new ValidOption(option.name))
      case _ => Left(new InvalidOption(token))
    }
  }

  private def parseLongOption(aggregate: InputParserResult, token: String): ParsedInput = {
    definition.getOption(token.drop(2)) match {
      case Some(option) => Right(new ValidOption(option.name))
      case _ => Left(new InvalidOption(token))
    }
  }
}
