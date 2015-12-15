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

  private var arguments = List[String]()
//  private var options = Map[String, ]

  type ParsedInput = Either[InvalidParameter, InputParameter]
  type ParsedInputList = List[ParsedInput]

  override def parse(): InputParserResult = {
    val parsedInput = parseInputArgs()
    val testing = foldTest()

    val (invalid, valid) = parsedInput
      .partition(_.isLeft)

    new InputParserResult(
      invalid.map(_.left.get).distinct,
      valid.map(_.right.get).distinct
    )
  }

  // todo: Aggregate, InputParserAggregate? ParserResultAggregate? Needs proper types.
  private case class FoldTestResult(args: List[String] = List[String](), opts: List[String] = List[String]())

  private def foldTest(): FoldTestResult = {
    args
      .takeWhile(_ != "--")
      .foldLeft(new FoldTestResult())((result, arg) => {
        new FoldTestResult(result.args :+ arg, result.opts)
      })
  }

  private def parseInputArgs(): ParsedInputList = {
    args
      .takeWhile(_ != "--")
      .map({
        case token if token.startsWith("--") =>
          parseLongOption(token)
        case token if token.startsWith("-") && token != "-" =>
          parseShortOption(token)
        case token =>
          parseArgument(token)
      })
      .toList
  }

  private def parseArgument(token: String): ParsedInput = {
    val index = arguments.size

    definition.getArgument(index) match {
      case Some(argument) => Right(argument)
      case _ => Left(new InvalidArgument(token))
    }
  }

  private def parseShortOption(token: String): ParsedInput = {
    definition.getOptionByShortName(token.drop(1)) match {
      case Some(option) => Right(option)
      case _ => Left(new InvalidOption(token))
    }
  }

  private def parseLongOption(token: String): ParsedInput = {
    definition.getOption(token.drop(2)) match {
      case Some(option) => Right(option)
      case _ => Left(new InvalidOption(token))
    }
  }
}
