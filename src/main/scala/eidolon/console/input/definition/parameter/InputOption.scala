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
 * Input Option
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 *
 * @param name A name
 * @param shortName A short name
 * @param mode A input argument mode
 * @param description A description
 * @param defaultValue A default value
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

  /**
   * Check if this input option accepts a value
   *
   * @return true if a value is accepted
   */
  def acceptValue: Boolean = {
    isRequiredValue || isOptionalValue
  }

  /**
   * Check if the input option must have no value
   *
   * @return true if the mode includes VALUE_NONE
   */
  def isNoValue: Boolean = {
    InputOption.VALUE_NONE == (InputOption.VALUE_NONE & mode)
  }

  /**
   * Check if the input option may optionally have a value
   *
   * @return true if the mode includes VALUE_OPTIONAL
   */
  def isOptionalValue: Boolean = {
    InputOption.VALUE_OPTIONAL == (InputOption.VALUE_OPTIONAL & mode)
  }

  /**
   * Check if the input option must have a value
   *
   * @return true if the mode includes VALUE_REQUIRED
   */
  def isRequiredValue: Boolean = {
    InputOption.VALUE_REQUIRED == (InputOption.VALUE_REQUIRED & mode)
  }

  /**
   * Check if this input option's mode is valid
   *
   * @return true if the mode is valid
   */
  private def isValidMode: Boolean = {
    (1 to 7).contains(mode)
  }

  /**
   * Check if this input option's name is valid
   *
   * @return true if the name is valid
   */
  private def isValidName: Boolean = {
    name.length > 0
  }

  /**
   * Check if this input option's short name is valid
   *
   * @return true if the short name is valid
   */
  private def isValidShortName: Boolean = {
    shortName.isEmpty || shortName.getOrElse("").length == 1
  }

  /**
   * Check if this input option's default value is valid
   *
   * @return true if the default value is valid
   */
  private def isValidDefaultValue: Boolean = {
    def validateNoneDefaultValue = {
      !isNoValue || defaultValue.isEmpty
    }

    def validateRequiredDefaultValue = {
      !isRequiredValue || defaultValue.isEmpty
    }

    validateNoneDefaultValue && validateRequiredDefaultValue
  }

  /**
   * Check if the given "copy" has the same values as this input option
   *
   * @return true if the values are the same
   */
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