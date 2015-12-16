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

import eidolon.console.input._

/**
 * Input Parser Result
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class InputParserResult(
    invalid: List[InvalidParameter] = List(),
    valid: List[ValidParameter] = List()) {

  private val invalidArguments = invalid.filter(_.isInstanceOf[InvalidArgument])
  private val invalidOptions = invalid.filter(_.isInstanceOf[InvalidOption])
  private val validArguments = valid.filter(_.isInstanceOf[ValidArgument])
  private val validOptions = valid.filter(_.isInstanceOf[ValidOption])

  lazy val argumentCount = invalidArguments.size + validArguments.size
  lazy val optionCount = invalidOptions.size + validOptions.size

  lazy val argumentNames = validArguments.map(_.name) ++: invalidArguments.map(_.token)
}
