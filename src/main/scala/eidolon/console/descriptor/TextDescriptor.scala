/**
 * This file is part of the "console" project.
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
import eidolon.console.input.definition.{InputArgument, InputDefinition, InputOption}
import eidolon.console.output.Output

/**
 * TextDescriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class TextDescriptor extends Descriptor {
  override def describeApplication(
      output: Output,
      application: Application)
    : Unit = {

    val commands = application.commands.values.toList.distinct

    // @todo: Add ability to have help text at an application level, conditionally show it here

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

    if (command.definition.options.nonEmpty || command.definition.arguments.nonEmpty) {
      val commandDefinition = new InputDefinition(
        command.definition.arguments,
        application.definition.options ++ command.definition.options
      )

      output.writeln("")
      describeInputDefinition(output, commandDefinition)
      output.writeln("")
    }

    if (command.help.nonEmpty) {
      output.writeln("")
      output.writeln("<comment>Help:</comment>")
      output.writeln("")
      output.write("  " + command.help.get.replace("\n", "\n  "))
    }

    output.writeln("")
  }

  override def describeInputDefinition(output: Output, definition: InputDefinition): Unit = {
    val argumentWidths = calculateArgumentWidths(definition.arguments.values.toList)
    val optionWidths = calculateOptionWidths(definition.options.values.toList)

    val totalWidth = (argumentWidths ++ optionWidths).reduce((a, b) => {
      if (a >= b) a else b
    })

    if (definition.arguments.nonEmpty) {
      output.writeln("<comment>Arguments:</comment>")
      output.writeln("")

      definition.arguments.values.foreach((argument) => {
        describeInputArgument(output, argument, totalWidth)
      })
    }

    if (definition.arguments.nonEmpty && definition.options.nonEmpty) {
      output.writeln("")
    }

    if (definition.options.nonEmpty) {
      output.writeln("<comment>Options:</comment>")
      output.writeln("")

      definition.options.values.foreach((option) => {
        describeInputOption(output, option, totalWidth)
      })
    }
  }

  override def describeInputArgument(output: Output, argument: InputArgument, totalWidth: Int): Unit = {
    val default = argument.default.nonEmpty match {
      case true => "<comment> [default: %s]</comment>".format(argument.default.get)
      case false => ""
    }

    val spacing = totalWidth - argument.name.length + 2

    output.writeln("  <info>%s</info>%s%s%s".format(
      argument.name,
      " " * spacing,
      argument.description.getOrElse(""),
      default
    ))
  }

  override def describeInputOption(output: Output, option: InputOption, totalWidth: Int): Unit = {
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

    val spacing = totalWidth - synopsis.length + 2

    output.writeln("  <info>%s</info>%s%s%s".format(
      synopsis,
      " " * spacing,
      option.description.getOrElse(""),
      default
    ))
  }

  private def calculateArgumentWidths(arguments: List[InputArgument]): List[Int] = {
    arguments.map((argument) => {
      argument.name.length
    })
  }

  private def calculateCommandWidths(commands: List[Command]): List[Int] = {
    commands.map((command) => {
      command.name.length
    })
  }

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

  private def getCommandSynopsis(command: Command, short: Boolean = false): String = {
    command.name + getInputDefinitionSynopsis(command.definition, short)
  }

  private def getInputDefinitionSynopsis(definition: InputDefinition, short: Boolean = false): String = {
    val options = getInputDefinitionOptionsSynopsis(definition, short)
    val arguments = getInputDefinitionArgumentsSynopsis(definition)

    options + arguments
  }

  private def getInputDefinitionOptionsSynopsis(definition: InputDefinition, short: Boolean): String = {
    short match {
      case true if definition.options.nonEmpty => " [options]"
      case false => definition.options.foldLeft("")({ case (synopsis, (name, option)) => {
        synopsis + getInputDefinitionOptionSynopsis(option)
      }})
      case _ => ""
    }
  }

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

  private def getInputDefinitionArgumentsSynopsis(definition: InputDefinition): String = {
    definition.arguments.foldLeft("")({ case (synopsis, (name, argument)) => {
      synopsis + getInputDefinitionArgumentSynopsis(argument)
    }})
  }

  private def getInputDefinitionArgumentSynopsis(argument: InputArgument): String = {
    val synopsis = s"<${argument.name}>"

    argument.isRequired match {
      case true => s" $synopsis"
      case false => s" [$synopsis]"
    }
  }

  private def getGlobalCommands(commands: List[Command]): List[Command] = {
    commands.filter((command) => {
      !isNamespacedCommand(command)
    })
  }

  private def getNamespacedCommands(commands: List[Command]): List[Command] = {
    commands.filter((command) => {
      isNamespacedCommand(command)
    })
  }

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

  private def isNamespacedCommand(command: Command): Boolean = {
    command.name.matches("^(.*):(.*)$")
  }
}
