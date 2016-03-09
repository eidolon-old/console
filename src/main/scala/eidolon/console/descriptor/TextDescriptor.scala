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

/**
 * Text Descriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class TextDescriptor extends Descriptor {
  /**
   * @inheritdoc
   */
  override def describeApplication(application: Application): String = {
    val commands = application.commands.values.toList.distinct
    val globalCommands = getGlobalCommands(commands).sortBy(_.name)
    val namespacedCommands = getNamespacedCommands(commands).sortBy(_.name)

    val totalWidth = calculateCommandWidths(commands).reduce((a, b) => {
      if (a >= b) a else b
    })

    val inputDefinitionDescription = describeInputDefinition(new InputDefinition(
      options = application.definition.options
    ))

    val globalCommandsDescription = describeApplicationGlobalCommands(globalCommands, namespacedCommands, totalWidth)
    val namespacedCommandsDescription = describeApplicationNamespacedCommands(globalCommands, namespacedCommands, totalWidth)

    s"""
       |<comment>Usage:</comment>
       |
       |  command [options] [arguments]
       |
       |$inputDefinitionDescription
       |<comment>Available commands:</comment>
       |
       |$globalCommandsDescription
       |$namespacedCommandsDescription""".stripMargin
  }

  /**
   * @inheritdoc
   */
  override def describeCommand(
      application: Application,
      command: Command)
    : String = {

    val synopsis = getCommandSynopsis(command, short = true)
    val aliases = command.aliases.foldLeft("")((aggregate, alias) => {
      aggregate + s"  $alias\n"
    })

    val argumentsDescription =
      command.definition.options.nonEmpty ||
      command.definition.arguments.nonEmpty match {
        case true =>
          val commandDefinition = new InputDefinition(
            command.definition.arguments,
            application.definition.options ++ command.definition.options
          )

          describeInputDefinition(commandDefinition)
        case _ => ""
      }

    val helpDescription = command.help.nonEmpty match {
      case true =>
        val commandHelp = command.help.get.replace("\n", "\n  ")

        s"""<comment>Help:</comment>
           |
           |  $commandHelp
           |
           |""".stripMargin
      case _ => ""
    }

    s"""<comment>Usage:</comment>
       |
       |  $synopsis
       |$aliases
       |$argumentsDescription
       |$helpDescription""".stripMargin
  }

  /**
   * @inheritdoc
   */
  override def describeInputDefinition(definition: InputDefinition): String = {
    val argumentsDescription: String = definition.arguments.nonEmpty match {
      case true =>
        val description = definition.arguments.foldLeft("")((aggregate, argument) => {
          aggregate + describeInputArgument(definition, argument) + "\n"
        })

        s"""<comment>Arguments:</comment>
           |
           |$description""".stripMargin
      case _ => ""
    }

    val spacer = definition.arguments.nonEmpty && definition.options.nonEmpty match {
      case true => "\n"
      case _ => ""
    }

    val optionsDescription = definition.options.nonEmpty match {
      case true =>
        val description = definition.options.foldLeft("")((aggregate, option) => {
          aggregate + describeInputOption(definition, option) + "\n"
        })

        s"""<comment>Options:</comment>
           |
           |$description""".stripMargin
      case _ => ""
    }

    argumentsDescription + spacer + optionsDescription
  }

  /**
   * @inheritdoc
   */
  override def describeInputArgument(
      definition: InputDefinition,
      argument: InputArgument)
    : String = {

    val default = argument.default.nonEmpty match {
      case true => "<comment> [default: %s]</comment>".format(argument.default.get)
      case _ => ""
    }

    val spacing = calculateDefinitionWidth(definition) - argument.name.length + 2

    "  <info>%s</info>%s%s%s".format(
      argument.name,
      " " * spacing,
      argument.description.getOrElse(""),
      default
    )
  }

  /**
   * @inheritdoc
   */
  override def describeInputOption(
      definition: InputDefinition,
      option: InputOption)
    : String = {

    val default = option.acceptsValue && option.defaultValue.nonEmpty match {
      case true => "<comment> [default: %s]</comment>".format(option.defaultValue.get)
      case false => ""
    }

    val name = "--%s".format(option.name)
    val shortName = option.shortName.nonEmpty match {
      case true => "-%s, ".format(option.shortName.get)
      case false => "    "
    }

    val value = option.acceptsValue match {
      case true if option.isOptionalValue => "[=%s]".format(option.name.toUpperCase)
      case true if !option.isOptionalValue => "=%s".format(option.name.toUpperCase)
      case false => ""
    }

    val synopsis = "%s%s%s".format(shortName, name, value)
    val spacing = calculateDefinitionWidth(definition) - synopsis.length + 2

    "  <info>%s</info>%s%s%s".format(
      synopsis,
      " " * spacing,
      option.description.getOrElse(""),
      default
    )
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
    val argumentWidths = calculateArgumentWidths(definition.arguments)
    val optionWidths = calculateOptionWidths(definition.options)

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
      val valueLength = option.acceptsValue match {
        case true if option.isOptionalValue => 3 + option.name.length
        case true if !option.isOptionalValue => 1 + option.name.length
        case false => 0
      }

      nameLength + valueLength
    })
  }

  /**
   * Describe the global commands within an application. Global commands are commands that are not
   * in any namespace.
   *
   * @return the description
   */
  private def describeApplicationGlobalCommands(
    globalCommands: List[Command],
    namespacedCommands: List[Command],
    totalWidth: Int
  ): String = {

    globalCommands.foldLeft("")((aggregate, command) => {
      val padding = if (namespacedCommands.nonEmpty) 4 else 2
      val spacing = totalWidth - command.name.length + padding

      aggregate + "  <info>%s</info>%s%s\n".format(
        command.name,
        " " * spacing,
        command.description.getOrElse("")
      )
    })
  }

  /**
   * Describe the namespaced commands within an application.
   *
   * @return the description
   */
  private def describeApplicationNamespacedCommands(
    globalCommands: List[Command],
    namespacedCommands: List[Command],
    totalWidth: Int
  ): String = {

    val (_, result) = namespacedCommands.foldLeft("" -> "")((aggregate, command) => {
      val namespace = aggregate._1
      val description = aggregate._2

      val commandNamespace = getCommandRootNamespace(command)
      val spacing = totalWidth - command.name.length + 2

      val namespaceDescription = if (commandNamespace != namespace) {
        s"  <comment>$commandNamespace</comment>\n"
      } else {
        ""
      }

      (commandNamespace, description + namespaceDescription + "    <info>%s</info>%s%s\n".format(
        command.name,
        " " * spacing,
        command.description.getOrElse("")
      ))
    })

    result
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
    definition.arguments.foldLeft("")({ case (synopsis, argument) =>
      synopsis + getInputDefinitionArgumentSynopsis(argument)
    })
  }

  /**
   * Get the synopsis of the given input argument for the input definitions' arguments synopsis
   *
   * @param argument The argument to get the synopsis for
   * @return An input definitions' argument synopsis
   */
  private def getInputDefinitionArgumentSynopsis(argument: InputArgument): String = {
    val synopsis = s"${argument.name}"

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
      case false => definition.options.foldLeft("")({ case (synopsis, option) =>
        synopsis + getInputDefinitionOptionSynopsis(option)
      })
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
    val value = option.acceptsValue match {
      case true =>
        "%s=%s%s".format(
          if (option.isOptionalValue) "[" else "",
          option.name.toUpperCase,
          if (option.isOptionalValue) "]" else ""
        )
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
   * @return The namespace of the given command
   * @throws UnsupportedOperationException if the given command doesn't have a namespace
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
