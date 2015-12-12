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

  override def parse(): InputParserResult = {
    val (errors, parsed) = args
      .takeWhile(_ != "--")
      .map({
        case token if token.startsWith("--") =>
          parseLongOption(token.drop(2))
        case token if token.startsWith("-") && token != "-" =>
          parseShortOption(token.drop(1))
        case token =>
          parseArgument(token)
      })
      .toList
      .partition(_.isLeft)

    new InputParserResult(
      errors.map(_.left.get),
      parsed.map(_.right.get)
    )
  }

  private def parseArgument(token: String): Either[InvalidArgument, InputArgument] = {
    definition.getArgument(token) match {
      case Some(argument) => Right(argument)
      case _ => Left(new InvalidArgument(token))
    }
  }

  private def parseShortOption(token: String): Either[InvalidOption, InputOption] = {
    definition.getOptionByShortName(token) match {
      case Some(option) => Right(option)
      case _ => Left(new InvalidOption(token))
    }
  }

  private def parseLongOption(token: String): Either[InvalidOption, InputOption] = {
    definition.getOption(token) match {
      case Some(option) => Right(option)
      case _ => Left(new InvalidOption(token))
    }
  }
}
