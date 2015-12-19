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
import eidolon.console.input.parser.ArgsInputParser

/**
 * Main
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object Main extends App {
  val definition = new InputDefinition()
    .withArgument(new InputArgument("first", mode = InputArgument.REQUIRED))
    .withArgument(new InputArgument("second", mode = InputArgument.REQUIRED))
    .withArgument(new InputArgument("hasDefault", default = Some("defaultTestValue")))
    .withOption(new InputOption("optional", Some("o")))
    .withOption(new InputOption("required", mode = InputOption.VALUE_REQUIRED))

  val parser = new ArgsInputParser(args, definition)
  val result = parser.parse()

  result match {
    case Left(errors) => {
      println("Errors:")
      println(errors)
    }
    case Right(input) => {
      println("Arguments:")
      println(input.arguments)

      println("Options:")
      println(input.options)

      println(input.hasOption("required"))
      println(input.getOptionValue("required"))
    }
  }
}
