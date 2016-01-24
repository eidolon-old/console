/**
 * This file is part of the "Default (Template) Project" project.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the LICENSE is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package eidolon.console.output.formatter

/**
 * Formatter
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait OutputFormatter[T <: OutputFormatter[T]] {
  val decorated: Boolean
  val styles: Map[String, OutputFormatterStyle]

  def format(message: String): String

  def hasStyle(name: String): Boolean = {
    styles.contains(name)
  }

  def withStyle(style: OutputFormatterStyle): T
}

object OutputFormatter {
  val TagInnerPattern = "[a-z][a-z0-9_=;-]*"
  val TagRegex = s"#<(($TagInnerPattern) | /($TagInnerPattern)?)>#ix"
}