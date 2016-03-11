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
 * Input
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 *
 * @param arguments A map of input arguments
 * @param options A map of input options
 */
class Input(
    val arguments: Map[String, String] = Map(),
    val options: Map[String, Option[String]] = Map()) {

  /**
   * Create a copy of this input with the given input argument
   *
   * @param name The input argument name
   * @param value The input argument value
   * @return a copy of the input
   */
  def withArgument(name: String, value: String): Input = {
    copy(arguments + (name -> value), options)
  }

  /**
   * Create a copy of this input with the given input arguments
   *
   * @param arguments The input arguments
   * @return a copy of the input
   */
  def withArguments(arguments: Map[String, String]): Input = {
    copy(this.arguments ++ arguments, options)
  }

  /**
   * Create a copy of this input with the given input option
   *
   * @param name The input option name
   * @param value The input option value
   * @return a copy of the input
   */
  def withOption(name: String, value: Option[String]): Input = {
    copy(arguments, options + (name -> value))
  }

  /**
   * Create a copy of this input with the given input options
   *
   * @param options The input options
   * @return a copy of the input
   */
  def withOptions(options: Map[String, Option[String]]): Input = {
    copy(arguments, this.options ++ options)
  }

  /**
   * Create a copy of this console input with the given arguments and options
   *
   * @param arguments A map of input arguments
   * @param options A map of input options
   * @return a copy of this console input
   */
  private def copy(
      arguments: Map[String, String],
      options: Map[String, Option[String]])
    : Input = {

    new Input(arguments, options)
  }
}
