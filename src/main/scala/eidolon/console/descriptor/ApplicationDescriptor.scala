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
import eidolon.console.input.definition.InputDefinition

/**
 * Application Descriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ApplicationDescriptor(
    inputDefinitionDescriptor: InputDefinitionDescriptor)
  extends Descriptor[Application]
  with DescriptorWidthCalculator {

  /**
   * @inheritdoc
   */
  override def describe(
      application: Application,
      definition: InputDefinition,
      entity: Application)
    : String = {

    val commands = application.commands.values.toList.distinct
    val globalCommands = getGlobalCommands(commands).sortBy(_.name)
    val namespacedCommands = getNamespacedCommands(commands).sortBy(_.name)

    val totalWidth = calculateCommandWidths(commands).reduce((a, b) => {
      if (a >= b) a else b
    })

    val inputDefinitionDescription = inputDefinitionDescriptor.describe(
      application,
      definition,
      new InputDefinition(
        options = application.definition.options
      )
    )

    val globalCommandsDescription =
      describeApplicationGlobalCommands(globalCommands, namespacedCommands, totalWidth)

    val namespacedCommandsDescription =
      describeApplicationNamespacedCommands(globalCommands, namespacedCommands, totalWidth)

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
   * Checks if the given command has a namespace
   *
   * @param command The command to check
   * @return True if the command has a namespace
   */
  private def isNamespacedCommand(command: Command): Boolean = {
    command.name.matches("^(.*):(.*)$")
  }
}
