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
case class Input(
    private val _arguments: Map[String, String],
    private val _options: Map[String, Option[String]]) {

  def arguments: Map[String, String] = {
    _arguments
  }

  def options: Map[String, Option[String]] = {
    _options
  }

  def getArgumentValue(name: String): Option[String] = {
    _arguments.get(name)
  }

  def getOptionValue(name: String): Option[String] = {
    _options.getOrElse(name, None)
  }

  def hasArgument(name: String): Boolean = {
    _arguments.get(name).nonEmpty
  }

  def hasOption(name: String): Boolean = {
    _options.get(name).nonEmpty
  }
}
