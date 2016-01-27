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
 */
trait Input {
  val arguments: Map[String, String]
  val options: Map[String, Option[String]]

  /**
   * Create a copy of this input with the given input argument
   *
   * @param name The input argument name
   * @param value The input argument value
   * @return a copy of the input
   */
  def withArgument(name: String, value: String): Input

  /**
   * Create a copy of this input with the given input option
   *
   * @param name The input option name
   * @param value The input option value
   * @return a copy of the input
   */
  def withOption(name: String, value: Option[String]): Input

  /**
   * Create a copy of this input with the given input arguments
   *
   * @param arguments The input arguments
   * @return a copy of the input
   */
  def withArguments(arguments: Map[String, String]): Input

  /**
   * Create a copy of this input with the given input options
   *
   * @param options The input options
   * @return a copy of the input
   */
  def withOptions(options: Map[String, Option[String]]): Input
}
