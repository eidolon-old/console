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
import eidolon.console.command.{Command, HelpCommand, ListCommand}
import eidolon.console.descriptor._
import eidolon.console.dialog.factory.DialogFactory
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.definition.parameter.{InputArgument, InputOption}
import eidolon.console.input.factory.InputFactory
import eidolon.console.input.parser.InputParser
import eidolon.console.input.parser.parameter.{ParsedInputArgument, ParsedInputParameter}
import eidolon.console.input.validation.InputValidator
import eidolon.console.output.Output
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
 *    val app = Application("myapp", "0.3.0-SNAPSHOT")
 *      .withCommand(new ExampleCommand())
 *
 *    app.run(args)
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 * @param name An application name
 * @param version An application version
 * @param inputParser An input parser
 * @param inputValidator An input validator
 * @param inputFactory An input factory
 * @param outputFactory An output factory
 * @param dialogFactory A dialog factory
 * @param userCommands A list of user commands
 */
class Application(
    val name: String,
    val version: String,
    val inputParser: InputParser,
    val inputValidator: InputValidator,
    val inputFactory: InputFactory,
    val outputFactory: OutputFactory,
    val dialogFactory: DialogFactory,
    val userCommands: List[Command[_]] = List()) {

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
   * @param args A list of raw input parameters
   * @return the resulting exit code
   */
  def run(args: List[String]): Int = {
    val parsedInput = inputParser.parse(args)
    val arguments = parsedInput.filter(_.isInstanceOf[ParsedInputArgument])

    if (arguments.nonEmpty && commands.exists(_.name == arguments.head.token)) {
      val command = commands.find(_.name == arguments.head.token).get

      doRunCommand(command, parsedInput)
    } else if (arguments.nonEmpty && commands.exists(_.aliases.contains(arguments.head.token))) {
      val command = commands.find(_.aliases.contains(arguments.head.token)).get

      doRunCommand(command, parsedInput)
    } else {
      doRunCommand(
        commands.find(_.name == "list").get,
        List(new ParsedInputArgument("list")) ++ parsedInput
      )
    }
  }

  /**
   * Run the application
   *
   * @param args An array of raw input parameters
   * @return the resulting exit code
   */
  def run(args: Array[String]): Int = {
    run(args.toList)
  }

  /**
   * Run a given command, with the given parsed input
   *
   * @param command A command to run
   * @param parsedInput Parsed input to run the command with
   * @return the resulting exit code
   */
  private def doRunCommand(command: Command[_], parsedInput: List[ParsedInputParameter]): Int = {
    val inputDefinition = definition ++ command.definition
    val validated = inputValidator.validate(inputDefinition, parsedInput)
    val wantsHelp = validated.validOptions.exists(_.name == "help")
    val wantsQuiet = validated.validOptions.exists(_.name == "quiet")
    val wantsVerbose = validated.validOptions.exists(_.name == "verbose")

    val verbosity = if (wantsQuiet) {
      Output.VerbosityQuiet
    } else if (wantsVerbose) {
      val verbosityOption = validated.validOptions.find(_.name == "verbose").get

      verbosityOption.value match {
        case Some("3") => Output.VerbosityDebug
        case Some("2") => Output.VerbosityVeryVerbose
        case _ => Output.VerbosityVerbose
      }
    } else {
      Output.VerbosityNormal
    }

    val input = inputFactory.build()
    val output = outputFactory.build(verbosity)
    val dialog = dialogFactory.build()

    validated.isValid match {
      case true if !wantsHelp => {
        val arguments = validated.validArguments.map(argument => argument.name -> argument.value)
        val options = validated.validOptions.map(option => option.name -> option.value)

        val commandInput = input
          .withArguments(arguments.toMap)
          .withOptions(options.toMap)

        command.run(commandInput, output, dialog)
      }
      case _ => {
        val helpCommand = commands.find(_.name == "help").get
        val commandInput = input.withArgument("command_name", command.name)

        helpCommand.run(commandInput, output, dialog)
      }
    }
  }

  /**
   * Build the built-in app commands
   *
   * @return a list of commands
   */
  protected def buildAppCommands(): List[Command[_]] = {
    val argumentDescriptor = new InputArgumentDescriptor()
    val optionDescriptor = new InputOptionDescriptor()
    val definitionDescriptor = new InputDefinitionDescriptor(argumentDescriptor, optionDescriptor)
    val commandDescriptor = new CommandDescriptor(definitionDescriptor)
    val applicationDescriptor = new ApplicationDescriptor(definitionDescriptor)

    val helpCommand = new HelpCommand(this, commandDescriptor)
    val listCommand = new ListCommand(this, applicationDescriptor)

    List(
      helpCommand,
      listCommand
    )
  }

  /**
   * Build the built-in input definition
   *
   * @return an input definition
   */
  protected def buildAppDefinition(): InputDefinition = {
    new InputDefinition()
      .withArgument(new InputArgument(
        "command",
        InputArgument.REQUIRED
      ))
      .withOption(new InputOption(
        "help",
        Some("h"),
        InputOption.VALUE_NONE,
        Some("Displays command help")
      ))
      .withOption(new InputOption(
        "quiet",
        Some("q"),
        InputOption.VALUE_NONE,
        Some("Reduces output")
      ))
      .withOption(new InputOption(
        "verbose",
        Some("v"),
        InputOption.VALUE_OPTIONAL,
        Some("Increases output, levels range from 1 to 3"),
        defaultValue = Some("1")
      ))
  }

  /**
   * Create a copy of this application with the given command
   *
   * @param command The command
   * @return a copy of the application
   */
  def withCommand(command: Command[_]): Application = {
    copy(userCommands :+ command)
  }

  /**
   * Create a copy of this application with the given commands
   *
   * @param commands The commands
   * @return a copy of the application
   */
  def withCommands(commands: List[Command[_]]): Application = {
    commands.foldLeft(this)((app, command) => {
      app.withCommand(command)
    })
  }

  /**
   * Create a copy of this application with the given user commands
   *
   * @param userCommands A list of user commands
   * @return a copy of this application
   */
  private def copy(userCommands: List[Command[_]]): Application = {
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

/**
 * Application Companion
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
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
      new OutputFactory(System.out, System.err, formatter),
      new DialogFactory(formatter)
    )
  }
}
