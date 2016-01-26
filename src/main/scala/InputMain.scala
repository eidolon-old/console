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

import java.text.SimpleDateFormat
import java.util.Calendar

import eidolon.chroma.Chroma
import eidolon.console.input.builder.InputBuilder
import eidolon.console.input.definition.{InputOption, InputDefinition, InputArgument}
import eidolon.console.input.parser.ArgsInputParser
import eidolon.console.input.validation.{InvalidOption, InvalidArgument, InputValidator}

/**
 * Main
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object InputMain /*extends App*/ {
  val definition = new InputDefinition()
    .withArgument(new InputArgument("first", mode = InputArgument.REQUIRED))
    .withArgument(new InputArgument("second", mode = InputArgument.REQUIRED))
    .withArgument(new InputArgument("hasDefault", default = Some("defaultTestValue")))
    .withOption(new InputOption("optional", Some("o")))
    .withOption(new InputOption("required", mode = InputOption.VALUE_REQUIRED))

  val args = Array[String]()

  val chroma = Chroma()
  val parser = new ArgsInputParser()
  val validator = new InputValidator()
  val builder = new InputBuilder()

  val parsed = parser.parse(args.toList)
  val validated = validator.validate(definition, parsed)

  if (validated.isValid) {
    val built = builder.build(validated)

    println("")
    println(chroma.green("Success!"))
    println("")
    println("Has argument 'first'? %s".format(chroma.blue(built.arguments.contains("first").toString)))
    println("Value: '%s'".format(chroma.green(built.arguments.get("first").toString)))
    println("")
    println("Has argument 'second'? %s".format(chroma.blue(built.arguments.contains("second").toString)))
    println("Value: '%s'".format(chroma.green(built.arguments.get("second").toString)))
    println("")
    println("Has argument 'hasDefault'? %s".format(chroma.blue(built.arguments.contains("hasDefault").toString)))
    println("Value: '%s'".format(chroma.green(built.arguments.get("hasDefault").toString)))
    println("")
    println("Has option 'optional'? %s".format(chroma.blue(built.options.contains("optional").toString)))
    println("Value: '%s'".format(chroma.green(built.options.getOrElse("optional", None).toString)))
    println("")
    println("Has option 'required'? %s".format(chroma.blue(built.options.contains("required").toString)))
    println("Value: '%s'".format(chroma.green(built.options.getOrElse("required", None).toString)))
    println("")
  } else {
    println(errorPrefix + "Invalid parameters found:")

    validated.invalid.foreach({
      case argument: InvalidArgument =>
        println(errorPrefix + "- Missing argument '%s'".format(argument.token))
      case option: InvalidOption =>
        println(errorPrefix + "- Unexpected option '%s'".format(option.token))
    })

    println(infoPrefix + "Try run with --help, or -h to display usage information.")
  }

  def infoPrefix: String = {
    chroma.blue(currentTime) + " [" + chroma.blue("INF") + "] "
  }

  def warningPrefix: String = {
    chroma.yellow(currentTime) + " [" + chroma.yellow("WRN") + "] "
  }

  def errorPrefix: String = {
    chroma.red(currentTime) + " [" + chroma.red("ERR") + "] "
  }

  def fatalPrefix: String = {
    chroma.bgRed.white.bold(currentTime + " [FTL]") + " "
  }

  def currentTime: String = {
    val format = new SimpleDateFormat("HH:mm:ss")
    val now = Calendar.getInstance.getTime

    format.format(now)
  }
}
