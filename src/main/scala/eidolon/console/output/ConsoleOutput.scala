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
 * Console Output
 *
 * For writing output to stdout
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 *
 * @param formatter An output formatter
 * @param verbosity The verbosity level of the output
 * @param errOutput Another output instance for writing to stderr
 */
class ConsoleOutput(
    override val formatter: OutputFormatter,
    override val verbosity: Int = Output.VerbosityNormal,
    val errOutput: Output)
  extends Output {

  /**
   * @inheritdoc
   */
  override protected def doWrite(message: String, newLine: Boolean): Unit = {
    if (newLine) {
      Console.out.println(message)
    } else {
      Console.out.print(message)
    }
  }
}
