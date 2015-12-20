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

package eidolon.console.input.builder

import eidolon.console.input.Input
import eidolon.console.input.validation.InputValidatorResult

/**
 * Input Builder
 *
 * Builds the final representation of the input parameters from the validated input parameters (this
 * includes the optional parameters with default values)
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputBuilder {
  def build(validatedInput: InputValidatorResult): Input = {
    new Input(
      validatedInput.validArguments.map(argument => argument.name -> argument.value).toMap,
      validatedInput.validOptions.map(option => option.name -> option.value).toMap
    )
  }
}
