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

/**
 * Args Input Parser
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
final class ArgsInputParser extends InputParser {
  override def parse(input: List[String]): List[ParsedInputParameter] = {
    input
      .takeWhile(_ != "--")
      .map({
        case option if option.startsWith("--") =>
          parseLongOption(option)
        case option if option.startsWith("-") && option != "-" =>
          parseShortOption(option)
        case argument =>
          parseArgument(argument)
      })
  }

  private def parseLongOption(option: String): ParsedInputLongOption = {
    val (name, value) = option
      .drop(2)
      .split("=", 2) match {
        case Array(name: String, value: String) =>
          (name, Some(value))
        case Array(name: String) =>
          (name, None)
        case _ =>
          // This should not be reachable, but if there's some kind of critical failure:
          throw new RuntimeException("Invalid parameter specified: '%s'".format(option))
      }

    new ParsedInputLongOption(name, value)
  }

  private def parseShortOption(option: String): ParsedInputShortOption = {
    val (name, value) = option
      .drop(1)
      .split("=", 2) match {
        case Array(name: String, value: String) =>
          (name, Some(value))
        case Array(name: String) =>
          (name, None)
        case _ =>
          // This should not be reachable, but if there's some kind of critical failure:
          throw new RuntimeException("Invalid parameter specified: '%s'".format(option))
      }

    new ParsedInputShortOption(name, value)
  }

  private def parseArgument(argument: String): ParsedInputArgument = {
    new ParsedInputArgument(argument)
  }
}
