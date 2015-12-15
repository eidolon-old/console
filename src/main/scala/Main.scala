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
import eidolon.console.input.validation._

/**
 * Main
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object Main extends App {
  val definition = new InputDefinition()
    .withArgument(new InputArgument("tester"))
    .withOption(new InputOption("tester", Some("t")))
    .withOption(new InputOption("doodoodoo", mode = InputOption.VALUE_REQUIRED))

  val parser = new ArgsInputParser(args, definition)
  val result = parser.parse()

  println("Invalid parameters:")
  println(result.invalid.map({
    case argument if argument.isInstanceOf[DuplicateArgument] =>
      s"Duplicate argument: ${argument.token}"
    case argument if argument.isInstanceOf[InvalidArgument] =>
      s"Invalid argument: ${argument.token}"
    case option if option.isInstanceOf[DuplicateOption] =>
      s"Duplicate option: ${option.token}"
    case option if option.isInstanceOf[InvalidOption] =>
      s"Invalid option: ${option.token}"
  }))

  println("Valid parameters:")
  println(result.valid.map({
    case argument if argument.isInstanceOf[InputArgument] =>
      s"Argument: ${argument.name}"
    case option if option.isInstanceOf[InputOption] =>
      s"Option: ${option.name}"
  }))
}
