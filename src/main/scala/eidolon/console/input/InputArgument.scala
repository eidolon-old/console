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

package eidolon.console.input

/**
 * Input Argument
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputArgument(
    val name: String,
    val mode: Int = InputArgument.OPTIONAL,
    val description: Option[String] = None,
    val default: Option[Any] = None)
  extends InputParameter {

  require(isValidMode, "Argument mode \"%d\" is not valid.".format(mode))
  require(isValidDefault, "Argument default is not valid.")

  def isArray: Boolean = {
    InputArgument.IS_ARRAY == (InputArgument.IS_ARRAY & mode)
  }

  def isOptional: Boolean = {
    InputArgument.OPTIONAL == (InputArgument.OPTIONAL & mode)
  }

  def isRequired: Boolean = {
    InputArgument.REQUIRED == (InputArgument.REQUIRED & mode)
  }

  private def isValidMode: Boolean = {
    (1 to 7).contains(mode)
  }

  private def isValidDefault: Boolean = {
    def validateRequiredDefault = {
      !isRequired || default.isEmpty
    }

    def validateArrayDefault = {
      !isArray || default.getOrElse(None).isInstanceOf[Array[Any]]
    }

    validateRequiredDefault && validateArrayDefault
  }
}

object InputArgument {
  val REQUIRED = 1
  val OPTIONAL = 2
  val IS_ARRAY = 4
}
