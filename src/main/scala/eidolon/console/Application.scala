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

import eidolon.chroma.Chroma
import eidolon.console.command.{ListCommand, Command, HelpCommand}
import eidolon.console.descriptor.TextDescriptor
import eidolon.console.dialog.factory.DialogFactory
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.definition.parameter.{InputOption, InputArgument}
import eidolon.console.input.factory.InputFactory
import eidolon.console.input.parser.InputParser
import eidolon.console.input.parser.parameter.{ParsedInputParameter, ParsedInputArgument}
import eidolon.console.input.validation.InputValidator
import eidolon.console.output.factory.OutputFactory
import eidolon.console.output.formatter.factory.ConsoleOutputFormatterFactory

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
 *    app.run(args.toList)
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 * @param name An application name
 * @param version An application version
 * @param inputParser An input parser
 * @param inputValidator An input validator
 * @param inputFactory An input factory
 * @param outputFactory An output factory
 * @param dialogFactory A dialog factory
 * @param userCommands A map of user commands
 */
class Application(
    val name: String,
    val version: String,
    val inputParser: InputParser,
    val inputValidator: InputValidator,
    val inputFactory: InputFactory,
    val outputFactory: OutputFactory,
    val dialogFactory: DialogFactory,
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

  /**
   * Run the application
   *
   * @param args A set of raw input parameters
   * @return the resulting exit code
   */
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

  /**
   * Run a given command, with the given parsed input
   *
   * @param command A command to run
   * @param parsedInput Parsed input to run the command with
   * @return the resulting exit code
   */
  private def doRunCommand(command: Command, parsedInput: List[ParsedInputParameter]): Int = {
    val inputDefinition = definition ++ command.definition
    val validated = inputValidator.validate(inputDefinition, parsedInput)

    if (validated.isValid) {
      val arguments = validated.validArguments.map(argument => argument.name -> argument.value)
      val options = validated.validOptions.map(option => option.name -> option.value)

      val input = inputFactory.build()
        .withArguments(arguments.toMap)
        .withOptions(options.toMap)
      val output = outputFactory.build()
      val dialog = dialogFactory.build()

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

  /**
   * Build the built-in app commands
   *
   * @return a map of commands
   */
  protected def buildAppCommands(): Map[String, Command] = {
    val descriptor = new TextDescriptor()

    val helpCommand = new HelpCommand(this, descriptor)
    val listCommand = new ListCommand(this, descriptor)

    Map(
      helpCommand.name -> helpCommand,
      listCommand.name -> listCommand
    )
  }

  /**
   * Build the built-in input definition
   *
   * @return an input definition
   */
  protected def buildAppDefinition(): InputDefinition = {
    new InputDefinition()
      .withArgument(new InputArgument("command", InputArgument.REQUIRED))
      .withOption(new InputOption("help", Some("h"), InputOption.VALUE_NONE, Some("Displays this help message")))
      .withOption(new InputOption("quiet", Some("q"), InputOption.VALUE_NONE, Some("Silence output")))
  }

  /**
   * Create a copy of this application with the given command
   *
   * @param command The command
   * @return a copy of the application
   */
  def withCommand(command: Command): Application = {
    copy(userCommands ++ command.aliases.map(_ -> command) + (command.name -> command))
  }

  /**
   * Create a copy of this application with the given commands
   *
   * @param commands The commands
   * @return a copy of the application
   */
  def withCommands(commands: List[Command]): Application = {
    commands.foldLeft(this)((app, command) => {
      app.withCommand(command)
    })
  }

  /**
   * Create a copy of this application with the given user commands
   *
   * @param userCommands A map of user commands
   * @return a copy of this application
   */
  private def copy(userCommands: Map[String, Command]): Application = {
    new Application(
      name,
      version,
      inputParser,
      inputValidator,
      inputFactory,
      outputFactory,
      dialogFactory,
      userCommands
    )
  }
}

object Application {
  /**
   * Create an application with the given name and version, and some sensible defaults for internals
   *
   * @param name An application name
   * @param version An application version
   * @return an application
   */
  def apply(name: String, version: String): Application = {
    val formatterFactory = new ConsoleOutputFormatterFactory(Chroma())
    val formatter = formatterFactory.build()

    new Application(
      name,
      version,
      new InputParser(),
      new InputValidator(),
      new InputFactory(),
      new OutputFactory(formatter),
      new DialogFactory(formatter)
    )
  }
}
