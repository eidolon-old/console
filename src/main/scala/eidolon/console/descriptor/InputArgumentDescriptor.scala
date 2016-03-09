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
import eidolon.console.input.definition.parameter.InputArgument

/**
 * Input Argument Descriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputArgumentDescriptor
  extends Descriptor[InputArgument]
  with DescriptorWidthCalculator {

  /**
   * @inheritdoc
   */
  override def describe(
      application: Application,
      definition: InputDefinition,
      entity: InputArgument)
    : String = {

    val spacing = calculateDefinitionWidth(definition) - entity.name.length + 2
    val default = entity.default.nonEmpty match {
      case true => "<comment> [default: %s]</comment>".format(entity.default.get)
      case _ => ""
    }

    "  <info>%s</info>%s%s%s".format(
      entity.name,
      " " * spacing,
      entity.description.getOrElse(""),
      default
    )
  }
}
