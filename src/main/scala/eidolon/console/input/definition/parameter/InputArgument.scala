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

package eidolon.console.input.definition.parameter

/**
 * Input Argument
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class InputArgument(
    override val name: String,
    mode: Int = InputArgument.OPTIONAL,
    description: Option[String] = None,
    default: Option[String] = None)
  extends InputParameter {

  require(isValidMode, "Argument mode \"%d\" is not valid.".format(mode))
  require(isValidDefault, "Argument default is not valid.")

  def hasDefault: Boolean = {
    default.nonEmpty
  }

  def isOptional: Boolean = {
    InputArgument.OPTIONAL == (InputArgument.OPTIONAL & mode)
  }

  def isRequired: Boolean = {
    InputArgument.REQUIRED == (InputArgument.REQUIRED & mode)
  }

  private def isValidMode: Boolean = {
    (1 to 3).contains(mode)
  }

  private def isValidDefault: Boolean = {
    !isRequired || default.isEmpty
  }
}

object InputArgument {
  val REQUIRED = 1
  val OPTIONAL = 2
}
