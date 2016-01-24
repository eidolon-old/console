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

package eidolon.console

import eidolon.console.command.{HelpCommand, Command}
import eidolon.console.input.builder.InputBuilder
import eidolon.console.input.definition.{InputOption, InputArgument, InputDefinition}
import eidolon.console.input.parser.{ArgsInputParser, ParsedInputArgument, ParsedInputParameter, InputParser}
import eidolon.console.input.validation.InputValidator
import eidolon.console.output.builder.{ConsoleOutputBuilder, OutputBuilder}

/**
 * Application
 *
 * The Application class serves as the entry point for a console application. It is what commands
 * are registered within for use.
 *
 * Usage (e.g. in main):
 *
 *    val app = Application("myapp", "0.1.0-SNAPSHOT", args)
 *      .withCommand(new ExampleCommand())
 *
 *    System.exit(app.run())
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class Application(
    val name: String,
    val version: String,
    val inputParser: InputParser,
    val inputValidator: InputValidator,
    val inputBuilder: InputBuilder,
    val outputBuilder: OutputBuilder,
    val commands: Map[String, Command] = Map()) {

  private val appCommands = buildAppCommands(commands)
  private val appDefinition = buildAppDefinition()


  def run(): Int = {
    val parsedInput = inputParser.parse()
    val arguments = parsedInput.filter(_.isInstanceOf[ParsedInputArgument])
    val allCommands = appCommands ++ commands

    if (arguments.nonEmpty && allCommands.contains(arguments.head.token)) {
      val command = allCommands.get(arguments.head.token).get

      doRunCommand(command, parsedInput)
    } else {
      val command = allCommands.get("help").get
      val helpInput = List(new ParsedInputArgument("help"))

      doRunCommand(command, helpInput)
    }
  }

  private def doRunCommand(command: Command, parsedInput: List[ParsedInputParameter]): Int = {
    val inputDefinition = appDefinition ++ command.definition
    val validated = inputValidator.validate(inputDefinition, parsedInput)

    if (validated.isValid) {
      val input = inputBuilder.build(validated)
      val output = outputBuilder.build()

      command.execute(input, output)
    } else {
      // Generate help text for the command
      println("Looks like you need help with command '%s'.".format(command.name))
    }

    0
  }

  protected def buildAppCommands(commands: Map[String, Command]): Map[String, Command] = {
    val helpCommand = new HelpCommand()

    Map(
      helpCommand.name -> helpCommand
    )
  }

  protected def buildAppDefinition(): InputDefinition = {
    new InputDefinition()
      .withArgument(new InputArgument("command", InputArgument.REQUIRED))
      .withOption(new InputOption("help", Some("h"), InputOption.VALUE_NONE, Some("Displays this help message")))
      .withOption(new InputOption("quiet", Some("q"), InputOption.VALUE_NONE, Some("Silence output")))
  }

  def withCommand(command: Command): Application = {
    copy(commands ++ command.aliases.map(_ -> command) + (command.name -> command))
  }

  private def copy(commands: Map[String, Command]): Application = {
    new Application(
      name,
      version,
      inputParser,
      inputValidator,
      inputBuilder,
      outputBuilder,
      commands = commands
    )
  }
}

object Application {
  def apply(name: String, version: String, args: Array[String]): Application = {
    new Application(
      name,
      version,
      new ArgsInputParser(args),
      new InputValidator(),
      new InputBuilder(),
      new ConsoleOutputBuilder()
    )
  }
}
