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
import eidolon.console.input.definition.InputDefinition

/**
 * Input Definition Descriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputDefinitionDescriptor(
    inputArgumentDescriptor: InputArgumentDescriptor,
    inputOptionDescriptor: InputOptionDescriptor)
  extends Descriptor[InputDefinition] {

  /**
   * @inheritdoc
   */
  override def describe(
      application: Application,
      definition: InputDefinition,
      entity: InputDefinition)
    : String = {

    val argumentsDescription: String = entity.arguments.nonEmpty match {
      case true =>
        val description = entity.arguments.foldLeft("")((aggregate, argument) => {
          aggregate + inputArgumentDescriptor.describe(application, entity, argument) + "\n"
        })

        s"""<comment>Arguments:</comment>
           |
           |$description""".stripMargin
      case _ => ""
    }

    val spacer = entity.arguments.nonEmpty && entity.options.nonEmpty match {
      case true => "\n"
      case _ => ""
    }

    val optionsDescription = entity.options.nonEmpty match {
      case true =>
        val description = entity.options.foldLeft("")((aggregate, option) => {
          aggregate + inputOptionDescriptor.describe(application, entity, option) + "\n"
        })

        s"""<comment>Options:</comment>
           |
           |$description""".stripMargin
      case _ => ""
    }

    argumentsDescription + spacer + optionsDescription
  }
}
