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

import eidolon.console.command.{ListCommand, Command, HelpCommand}
import eidolon.console.dialog.builder.{ConsoleDialogBuilder, DialogBuilder}
import eidolon.console.input.builder.InputBuilder
import eidolon.console.input.definition.{InputArgument, InputDefinition, InputOption}
import eidolon.console.input.parser.{ArgsInputParser, InputParser, ParsedInputArgument, ParsedInputParameter}
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
 *    val app = Application("myapp", "0.1.0-SNAPSHOT")
 *      .withCommand(new ExampleCommand())
 *
 *    app.run(args)
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
    val dialogBuilder: DialogBuilder,
    val userCommands: Map[String, Command] = Map()) {

  private val appCommands = buildAppCommands()

  val commands = appCommands ++ userCommands
  val definition = buildAppDefinition()

  val logo =
    """                                             #
      |                              ###            ##
      |######## ### ####### #######  ###   #######  ###  ##
      |         ###       ##      ## ###         ## #### ##
      | ####### ###  ###  ## ##   ## ###    ##   ## #######
      | ###     ###  ###  ## ##   ## ###    ##   ## ### ###
      | ####### ###  ######   #####  ####### #####  ###  ##
      |                                                   #
    """.stripMargin

  def run(args: List[String]): Int = {
    val parsedInput = inputParser.parse(args)
    val arguments = parsedInput.filter(_.isInstanceOf[ParsedInputArgument])

    if (arguments.nonEmpty && commands.contains(arguments.head.token)) {
      val command = commands.get(arguments.head.token).get

      doRunCommand(command, parsedInput)
    } else {
      doRunCommand(commands.get("list").get, List(new ParsedInputArgument("list")))
    }
  }

  private def doRunCommand(command: Command, parsedInput: List[ParsedInputParameter]): Int = {
    val inputDefinition = definition ++ command.definition
    val validated = inputValidator.validate(inputDefinition, parsedInput)

    if (validated.isValid) {
      val input = inputBuilder.build(validated)
      val output = outputBuilder.build()
      val dialog = dialogBuilder.build()

      command.execute(input, output, dialog)
    } else {
      doRunCommand(
        commands.get("help").get,
        List(
          new ParsedInputArgument("help"),
          new ParsedInputArgument(command.name)
        )
      )
    }

    0
  }

  protected def buildAppCommands(): Map[String, Command] = {
    val helpCommand = new HelpCommand(this)
    val listCommand = new ListCommand(this)

    Map(
      helpCommand.name -> helpCommand,
      listCommand.name -> listCommand
    )
  }

  protected def buildAppDefinition(): InputDefinition = {
    new InputDefinition()
      .withArgument(new InputArgument("command", InputArgument.REQUIRED))
      .withOption(new InputOption("help", Some("h"), InputOption.VALUE_NONE, Some("Displays this help message")))
      .withOption(new InputOption("quiet", Some("q"), InputOption.VALUE_NONE, Some("Silence output")))
  }

  def withCommand(command: Command): Application = {
    copy(userCommands ++ command.aliases.map(_ -> command) + (command.name -> command))
  }

  private def copy(userCommands: Map[String, Command]): Application = {
    new Application(
      name,
      version,
      inputParser,
      inputValidator,
      inputBuilder,
      outputBuilder,
      dialogBuilder,
      userCommands
    )
  }
}

object Application {
  def apply(name: String, version: String): Application = {
    new Application(
      name,
      version,
      new ArgsInputParser(),
      new InputValidator(),
      new InputBuilder(),
      new ConsoleOutputBuilder(),
      new ConsoleDialogBuilder()
    )
  }
}
