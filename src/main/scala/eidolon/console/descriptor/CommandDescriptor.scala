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
import eidolon.console.input.definition.parameter.InputArgument

/**
 * Command Descriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class CommandDescriptor(
    inputDefinitionDescriptor: InputDefinitionDescriptor)
  extends Descriptor[Command]
  with DescriptorWidthCalculator {

  /**
   * @inheritdoc
   */
  override def describe(
      application: Application,
      definition: InputDefinition,
      entity: Command)
    : String = {

    val synopsis = getCommandSynopsis(entity)
    val aliases = entity.aliases.foldLeft("")((aggregate, alias) => {
      aggregate + s"  $alias\n"
    })

    val argumentsDescription =
      entity.definition.options.nonEmpty ||
      entity.definition.arguments.nonEmpty match {
        case true =>
          val commandDefinition = new InputDefinition(
            entity.definition.arguments,
            application.definition.options ++ entity.definition.options
          )

          inputDefinitionDescriptor.describe(application, definition, commandDefinition)
        case _ => ""
      }

    val helpDescription = entity.help.nonEmpty match {
      case true =>
        val commandHelp = entity.help.get.replace("\n", "\n  ")

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
   * Get the synopsis of a given command
   *
   * @param command The command to get the synopsis for
   * @return A command synopsis
   */
  private def getCommandSynopsis(command: Command): String = {
    command.name + getInputDefinitionSynopsis(command.definition)
  }

  /**
   * Get the synopsis of a given input definition
   *
   * @param definition The input definition to get the synopsis for
   * @param short Whether or not to show a short synopsis (less info about options in synopsis)
   * @return An input definition synopsis
   */
  private def getInputDefinitionSynopsis(definition: InputDefinition): String = {
    val options = getInputDefinitionOptionsSynopsis(definition)
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
   * @return An input definitions' options synopsis
   */
  private def getInputDefinitionOptionsSynopsis(definition: InputDefinition): String = {
    if (definition.options.nonEmpty) " [options]" else ""
  }
}
