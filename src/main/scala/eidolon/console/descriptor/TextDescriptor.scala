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

package eidolon.console.descriptor

import eidolon.console.Application
import eidolon.console.command.Command
import eidolon.console.input.definition.parameter.{InputOption, InputArgument}
import eidolon.console.input.definition.InputDefinition
import eidolon.console.output.Output

/**
 * TextDescriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class TextDescriptor extends Descriptor {
  /**
   * @inheritdoc
   */
  override def describeApplication(
      output: Output,
      application: Application)
    : Unit = {

    val commands = application.commands.values.toList.distinct

    output.writeln("<comment>Usage:</comment>")
    output.writeln("")
    output.writeln("  command [options] [arguments]")
    output.writeln("")
    describeInputDefinition(output, new InputDefinition(options = application.definition.options))
    output.writeln("")

    val totalWidth = calculateCommandWidths(commands).reduce((a, b) => {
      if (a >= b) a else b
    })

    output.writeln("<comment>Available commands:</comment>")
    output.writeln("")

    val globalCommands = getGlobalCommands(commands).sortBy(_.name)
    val namespacedCommands = getNamespacedCommands(commands).sortBy(_.name)

    globalCommands.foreach((command) => {
      val padding = if (namespacedCommands.nonEmpty) 4 else 2
      val spacing = totalWidth - command.name.length + padding

      output.writeln("  <info>%s</info>%s%s".format(
        command.name,
        " " * spacing,
        command.description.getOrElse("")
      ))
    })

    if (namespacedCommands.nonEmpty) {
      namespacedCommands.foldLeft("")((namespace, command) => {
        val commandNamespace = getCommandRootNamespace(command)
        val spacing = totalWidth - command.name.length + 2

        if (commandNamespace != namespace) {
          output.writeln(s"  <comment>$commandNamespace</comment>")
        }

        output.writeln("    <info>%s</info>%s%s".format(
          command.name,
          " " * spacing,
          command.description.getOrElse("")
        ))

        commandNamespace
      })
    }

    output.writeln("")
  }

  /**
   * @inheritdoc
   */
  override def describeCommand(
      output: Output,
      application: Application,
      command: Command)
    : Unit = {

    output.writeln("<comment>Usage:</comment>")
    output.writeln("")
    output.write("  " + getCommandSynopsis(command, short = true), mode = Output.OutputRaw)

    command.aliases.foreach((alias) => {
      output.writeln("  " + alias)
    })

    output.writeln("")

    if (command.definition.options.nonEmpty || command.definition.arguments.nonEmpty) {
      val commandDefinition = new InputDefinition(
        command.definition.arguments,
        application.definition.options ++ command.definition.options
      )

      describeInputDefinition(output, commandDefinition)
    }

    if (command.help.nonEmpty) {
      output.writeln("")
      output.writeln("<comment>Help:</comment>")
      output.writeln("")
      output.writeln("  " + command.help.get.replace("\n", "\n  "))
    }

    output.writeln("")
  }

  /**
   * @inheritdoc
   */
  override def describeInputDefinition(output: Output, definition: InputDefinition): Unit = {
    if (definition.arguments.nonEmpty) {
      output.writeln("<comment>Arguments:</comment>")
      output.writeln("")

      definition.arguments.values.foreach((argument) => {
        describeInputArgument(output, definition, argument)
      })
    }

    if (definition.arguments.nonEmpty && definition.options.nonEmpty) {
      output.writeln("")
    }

    if (definition.options.nonEmpty) {
      output.writeln("<comment>Options:</comment>")
      output.writeln("")

      definition.options.values.foreach((option) => {
        describeInputOption(output, definition, option)
      })
    }
  }

  /**
   * @inheritdoc
   */
  override def describeInputArgument(
      output: Output,
      definition: InputDefinition,
      argument: InputArgument)
    : Unit = {

    val default = argument.default.nonEmpty match {
      case true => "<comment> [default: %s]</comment>".format(argument.default.get)
      case false => ""
    }

    val spacing = calculateDefinitionWidth(definition) - argument.name.length + 2

    output.writeln("  <info>%s</info>%s%s%s".format(
      argument.name,
      " " * spacing,
      argument.description.getOrElse(""),
      default
    ))
  }

  /**
   * @inheritdoc
   */
  override def describeInputOption(
      output: Output,
      definition: InputDefinition,
      option: InputOption)
    : Unit = {

    val default = option.acceptValue && option.defaultValue.nonEmpty match {
      case true => "<comment> [default: %s]</comment>".format(option.defaultValue.get)
      case false => ""
    }

    val name = "--%s".format(option.name)
    val shortName = option.shortName.nonEmpty match {
      case true => "-%s, ".format(option.shortName.get)
      case false => "    "
    }

    val value = option.acceptValue match {
      case true if option.isOptionalValue => "[=%s]".format(option.name.toUpperCase)
      case true if !option.isOptionalValue => "=%s".format(option.name.toUpperCase)
      case false => ""
    }

    val synopsis = "%s%s%s".format(shortName, name, value)
    val spacing = calculateDefinitionWidth(definition) - synopsis.length + 2

    output.writeln("  <info>%s</info>%s%s%s".format(
      synopsis,
      " " * spacing,
      option.description.getOrElse(""),
      default
    ))
  }

  /**
   * Calculate the widths of all of the given input arguments
   *
   * @param arguments The input arguments to calculate the widths of
   * @return A list of input argument widths
   */
  private def calculateArgumentWidths(arguments: List[InputArgument]): List[Int] = {
    arguments.map((argument) => {
      argument.name.length
    })
  }

  /**
   * Calculate the widths of all of the given commands
   *
   * @param commands The commands to calculate the widths of
   * @return A list of command widths
   */
  private def calculateCommandWidths(commands: List[Command]): List[Int] = {
    commands.map((command) => {
      command.name.length
    })
  }

  /**
   * Calulcate the total width of the synopsi for the given input definition's arguments and options
   *
   * @param definition The definition to calculate the width of
   * @return The width
   */
  private def calculateDefinitionWidth(definition: InputDefinition): Int = {
    val argumentWidths = calculateArgumentWidths(definition.arguments.values.toList)
    val optionWidths = calculateOptionWidths(definition.options.values.toList)

    (argumentWidths ++ optionWidths).reduce((a, b) => {
      if (a >= b) a else b
    })
  }

  /**
   * Calculate the widths of all of the given input options
   *
   * @param options The input options to calculate the widths of
   * @return A list of input option widths
   */
  private def calculateOptionWidths(options: List[InputOption]): List[Int] = {
    options.map((option) => {
      // "-" + shortName + ", --" + name
      val nameLength = 1 + math.max(option.shortName.getOrElse("").length, 1) + 4 + option.name.length
      val valueLength = option.acceptValue match {
        case true if option.isOptionalValue => 3 + option.name.length
        case true if !option.isOptionalValue => 1 + option.name.length
        case false => 0
      }

      nameLength + valueLength
    })
  }

  /**
   * Get the synopsis of a given command
   *
   * @param command The command to get the synopsis for
   * @param short Whether or not to show a short synopsis (less info about options in synopsis)
   * @return A command synopsis
   */
  private def getCommandSynopsis(command: Command, short: Boolean = false): String = {
    command.name + getInputDefinitionSynopsis(command.definition, short)
  }

  /**
   * Get the synopsis of a given input definition
   *
   * @param definition The input definition to get the synopsis for
   * @param short Whether or not to show a short synopsis (less info about options in synopsis)
   * @return An input definition synopsis
   */
  private def getInputDefinitionSynopsis(definition: InputDefinition, short: Boolean = false): String = {
    val options = getInputDefinitionOptionsSynopsis(definition, short)
    val arguments = getInputDefinitionArgumentsSynopsis(definition)

    options + arguments
  }

  /**
   * Get the synopsis of the input arguments in the given input definition
   *
   * @param definition The input definition to get the arguments from
   * @return An input definitions' arguments synopsis
   */
  private def getInputDefinitionArgumentsSynopsis(definition: InputDefinition): String = {
    definition.arguments.foldLeft("")({ case (synopsis, (name, argument)) => {
      synopsis + getInputDefinitionArgumentSynopsis(argument)
    }})
  }

  /**
   * Get the synopsis of the given input argument for the input definitions' arguments synopsis
   *
   * @param argument The argument to get the synopsis for
   * @return An input definitions' argument synopsis
   */
  private def getInputDefinitionArgumentSynopsis(argument: InputArgument): String = {
    val synopsis = s"<${argument.name}>"

    argument.isRequired match {
      case true => s" $synopsis"
      case false => s" [$synopsis]"
    }
  }

  /**
   * Get the synopsis of the input options in the given input definition
   *
   * @param definition The input definition to get the options from
   * @param short Whether or not to show a short synopsis (less info about options in synopsis)
   * @return An input definitions' options synopsis
   */
  private def getInputDefinitionOptionsSynopsis(definition: InputDefinition, short: Boolean): String = {
    short match {
      case true if definition.options.nonEmpty => " [options]"
      case false => definition.options.foldLeft("")({ case (synopsis, (name, option)) => {
        synopsis + getInputDefinitionOptionSynopsis(option)
      }})
      case _ => ""
    }
  }

  /**
   * Get the synopsis of the given input option for the input definitions' options synopsis
   *
   * @param option The option to get the synopsis for
   * @return An input definitions' option synopsis
   */
  private def getInputDefinitionOptionSynopsis(option: InputOption): String = {
    val value = option.acceptValue match {
      case true => {
        "%s=%s%s".format(
          if (option.isOptionalValue) "[" else "",
          option.name.toUpperCase,
          if (option.isOptionalValue) "]" else ""
        )
      }
      case false => ""
    }

    val shortName = option.shortName.isDefined match {
      case true => "-%s|".format(option.shortName.get)
      case false => ""
    }

    " [%s--%s%s]".format(shortName, option.name, value)
  }

  /**
   * Get commands that don't reside in any namespace
   *
   * @param commands A list of commands
   * @return A list of commands without a namespace
   */
  private def getGlobalCommands(commands: List[Command]): List[Command] = {
    commands.filter((command) => {
      !isNamespacedCommand(command)
    })
  }

  /**
   * Get commands that do reside in a namespace
   *
   * @param commands A list of commands
   * @return A list of commands with a namespace
   */
  private def getNamespacedCommands(commands: List[Command]): List[Command] = {
    commands.filter((command) => {
      isNamespacedCommand(command)
    })
  }

  /**
   * Get the root namespace of a given command
   *
   * @param command The command to get the namespace for
   * @throws UnsupportedOperationException if the given command doesn't have a namespace
   * @return The namespace of the given command
   */
  @throws[UnsupportedOperationException]
  private def getCommandRootNamespace(command: Command): String = {
    val pattern = "^([A-Za-z0-9_-]+):(.+)$".r.unanchored

    command.name match {
      case pattern(namespace, _) => namespace
      case _ => throw new UnsupportedOperationException(
        "Cannot get namespace for command with name '%s'".format(command.name)
      )
    }
  }

  /**
   * Checks if the given command has a namespace
   *
   * @param command The command to check
   * @return True if the command has a namespace
   */
  private def isNamespacedCommand(command: Command): Boolean = {
    command.name.matches("^(.*):(.*)$")
  }
}
