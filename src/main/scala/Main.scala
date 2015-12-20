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

import eidolon.console.input.builder.InputBuilder
import eidolon.console.input.definition.{InputOption, InputDefinition, InputArgument}
import eidolon.console.input.parser.ArgsInputParser
import eidolon.console.input.validation.InputValidator

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

  val parser = new ArgsInputParser(args)
  val validator = new InputValidator()
  val builder = new InputBuilder()

  val parsed = parser.parse()
  val validated = validator.validate(definition, parsed)
  val built = builder.build(validated)

  if (validated.isValid) {
    println("")
    println("Success!")
    println("")
    println("Has argument 'first'? %s".format(built.hasArgument("first")))
    println("Value: '%s'".format(built.getArgumentValue("first").getOrElse("")))
    println("")
    println("Has argument 'second'? %s".format(built.hasArgument("second")))
    println("Value: '%s'".format(built.getArgumentValue("second").getOrElse("")))
    println("")
    println("Has argument 'hasDefault'? %s".format(built.hasArgument("hasDefault")))
    println("Value: '%s'".format(built.getArgumentValue("hasDefault").getOrElse("")))
    println("")
    println("Has option 'optional'? %s".format(built.hasOption("optional")))
    println("Value: '%s'".format(built.getOptionValue("optional")))
    println("")
    println("Has option 'required'? %s".format(built.hasOption("required")))
    println("Value: '%s'".format(built.getOptionValue("required")))
    println("")
  } else {
    println("Some invalid parameters were specified, please check your input and try again.")
    println(validated.invalid)
  }
}
