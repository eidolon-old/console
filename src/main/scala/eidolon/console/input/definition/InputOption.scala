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

package eidolon.console.input.definition

/**
 * Input Option
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class InputOption(
    override val name: String,
    shortName: Option[String] = None,
    mode: Int = InputOption.VALUE_NONE,
    description: Option[String] = None,
    defaultValue: Option[String] = None)
  extends InputParameter {

  require(isValidName, "Option name \"%s\" is not valid.".format(name))
  require(isValidShortName, "Option short name \"%s\" is not valid.".format(shortName))
  require(isValidMode, "Option mode \"%d\" is not valid.".format(mode))
  require(isValidDefaultValue, "Option defaultValue is not valid.")

  def acceptValue: Boolean = {
    isRequiredValue || isOptionalValue
  }

  def isNoValue: Boolean = {
    InputOption.VALUE_NONE == (InputOption.VALUE_NONE & mode)
  }

  def isOptionalValue: Boolean = {
    InputOption.VALUE_OPTIONAL == (InputOption.VALUE_OPTIONAL & mode)
  }

  def isRequiredValue: Boolean = {
    InputOption.VALUE_REQUIRED == (InputOption.VALUE_REQUIRED & mode)
  }

  private def isValidMode: Boolean = {
    (1 to 7).contains(mode)
  }

  private def isValidName: Boolean = {
    name.length > 0
  }

  private def isValidShortName: Boolean = {
    shortName.isEmpty || shortName.getOrElse("").length == 1
  }

  private def isValidDefaultValue: Boolean = {
    def validateNoneDefaultValue = {
      !isNoValue || defaultValue.isEmpty
    }

    def validateRequiredDefaultValue = {
      !isRequiredValue || defaultValue.isEmpty
    }

    validateNoneDefaultValue && validateRequiredDefaultValue
  }

  override def equals(copy: Any): Boolean = { copy match {
    case copy: InputOption =>
      name == copy.name &&
      shortName == copy.shortName &&
      mode == copy.mode &&
      description == copy.description &&
      defaultValue == copy.defaultValue
    case _ => false
  }}
}

object InputOption {
  val VALUE_NONE = 1
  val VALUE_REQUIRED = 2
  val VALUE_OPTIONAL = 4
}