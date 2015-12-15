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

import eidolon.console.input.{InputOption, InputArgument, InputParameter}

/**
 * Input Parser Result
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class InputParserResult(
    invalid: List[InvalidParameter] = List(),
    valid: List[InputParameter] = List()) {

  private lazy val invalidArgumentCount = invalid
    .foldLeft(0)((count, item) => item match {
      case argument if argument.isInstanceOf[InvalidArgument] => count + 1
      case _ => count
    })

  private lazy val invalidOptionCount = invalid
    .foldLeft(0)((count, item) => item match {
      case option if option.isInstanceOf[InvalidOption] => count + 1
      case _ => count
    })

  private lazy val validArgumentCount = valid
    .foldLeft(0)((count, item) => item match {
      case argument if argument.isInstanceOf[InputArgument] => count + 1
      case _ => count
    })

  private lazy val validOptionCount = valid
    .foldLeft(0)((count, item) => item match {
      case option if option.isInstanceOf[InputOption] => count + 1
      case _ => count
    })

  lazy val argumentCount = invalidArgumentCount + validArgumentCount
  lazy val optionCount = invalidOptionCount + validOptionCount
}
