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

package eidolon.console.output

import eidolon.console.output.formatter.OutputFormatter

/**
 * Console Error Output
 *
 * For writing output to stderr
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 *
 * @param formatter An output formatter
 * @param verbosity The verbosity level of the output
 */
class ConsoleErrorOutput(
    override val formatter: OutputFormatter,
    override val verbosity: Int = Output.VerbosityNormal)
  extends Output {

  /**
   * @inheritdoc
   */
  override protected def doWrite(message: String, newLine: Boolean): Unit = {
    if (newLine) {
      Console.err.println(message)
    } else {
      Console.err.print(message)
    }
  }
}
