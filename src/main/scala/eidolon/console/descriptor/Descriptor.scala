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
 * Entity Descriptor
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait Descriptor[E] {
  /**
   * Describe the entity
   *
   * @return the description
   */
  def describe(
      application: Application,
      definition: InputDefinition,
      entity: E)
    : String
}
