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

package eidolon.console.output.factory

import eidolon.console.output.Output
import eidolon.console.output.formatter.OutputFormatter
import java.io.PrintStream

/**
 * Output Factory
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 * @param outStream the standard output stream
 * @param errStream the error output stream
 * @param formatter the output formatter
 */
class OutputFactory(
    outStream: PrintStream,
    errStream: PrintStream,
    formatter: OutputFormatter) {

  /**
   * Build Output
   *
   * @param verbosity a verbosity level
   * @return an Output instance
   */
  def build(verbosity: Int): Output = {
    new Output(
      outStream,
      errStream,
      formatter,
      verbosity
    )
  }
}
