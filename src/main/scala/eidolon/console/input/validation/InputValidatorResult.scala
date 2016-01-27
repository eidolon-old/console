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

package eidolon.console.input.validation

import eidolon.console.input.validation.parameter._

/**
 * Input Validator Result
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class InputValidatorResult(
    invalid: List[InvalidParameter] = List(),
    valid: List[ValidParameter] = List()) {

  lazy val invalidArguments = invalid.filter(_.isInstanceOf[InvalidArgument]).asInstanceOf[List[InvalidArgument]]
  lazy val invalidOptions = invalid.filter(_.isInstanceOf[InvalidOption]).asInstanceOf[List[InvalidOption]]
  lazy val validArguments = valid.filter(_.isInstanceOf[ValidArgument]).asInstanceOf[List[ValidArgument]]
  lazy val validOptions = valid.filter(_.isInstanceOf[ValidOption]).asInstanceOf[List[ValidOption]]

  lazy val argumentCount = invalidArguments.size + validArguments.size
  lazy val optionCount = invalidOptions.size + validOptions.size

  lazy val invalidCount = invalidArguments.size + invalidOptions.size
  lazy val validCount = validArguments.size + validOptions.size

  lazy val argumentNames = validArguments.map(_.name) ++: invalidArguments.map(_.token)
  lazy val optionNames = validOptions.map(_.name) ++: invalidArguments.map(_.token)

  /**
   * Check if this input validator result reports valid input or not
   *
   * @return true if the result is that the input is valid
   */
  def isValid: Boolean = {
    invalid.isEmpty
  }
}
