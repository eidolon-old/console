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

  def withArgument(name: String, value: String): Input
  def withOption(name: String, value: Option[String]): Input

  def withArguments(arguments: Map[String, String]): Input
  def withOptions(options: Map[String, Option[String]]): Input
}
