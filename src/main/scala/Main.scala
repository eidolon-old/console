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

import eidolon.console.input._
import eidolon.console.input.validation.{InvalidOption, InvalidArgument}

/**
 * Main
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object Main extends App {
  val definition = new InputDefinition()
    .withArgument(new InputArgument("tester"))
    .withOption(new InputOption("tester", Some("t")))

  val parser = new ArgsInputParser(args, definition)
  val result = parser.parse()

  // println(definition)
  println("Invalid parameters:")
  println(result.errors.map({
    case argument if argument.isInstanceOf[InvalidArgument] =>
      s"Argument: ${argument.token}"
    case option if option.isInstanceOf[InvalidOption] =>
      s"Option: ${option.token}"
  }))

  println("Valid parameters:")
  println(result.parsed.map({
    case argument if argument.isInstanceOf[InputArgument] =>
      s"Argument: ${argument.name}"
    case option if option.isInstanceOf[InputOption] =>
      s"Option: ${option.name}"
  }))

  println("Number of duplicates:")
  println(result.parsed.size - result.parsed.distinct.size)
}
