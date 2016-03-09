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
import eidolon.console.input.definition.parameter.InputOption

/**
 * Input Option Descriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputOptionDescriptor
  extends Descriptor[InputOption]
  with DescriptorWidthCalculator {

  /**
   * @inheritdoc
   */
  override def describe(
      application: Application,
      definition: InputDefinition,
      entity: InputOption)
    : String = {

    val name = "--%s".format(entity.name)
    val shortName = entity.shortName.nonEmpty match {
      case true => "-%s, ".format(entity.shortName.get)
      case false => "    "
    }

    val value = entity.acceptsValue match {
      case true if entity.isOptionalValue => "[=%s]".format(entity.name.toUpperCase)
      case true if !entity.isOptionalValue => "=%s".format(entity.name.toUpperCase)
      case false => ""
    }

    val synopsis = "%s%s%s".format(shortName, name, value)
    val spacing = calculateDefinitionWidth(definition) - synopsis.length + 2

    val default = entity.acceptsValue && entity.defaultValue.nonEmpty match {
      case true => "<comment> [default: %s]</comment>".format(entity.defaultValue.get)
      case false => ""
    }

    "  <info>%s</info>%s%s%s".format(
      synopsis,
      " " * spacing,
      entity.description.getOrElse(""),
      default
    )
  }
}
