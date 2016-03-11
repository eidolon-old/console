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
import eidolon.console.output.writer.PrintStreamOutputWriter

/**
 * Output Factory
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class OutputFactory(formatter: OutputFormatter) {
  /**
   * Build output
   *
   * @return an output instance
   */
  def build(): Output = {
    val outWriter = new PrintStreamOutputWriter(formatter, System.out)
    val errWriter = new PrintStreamOutputWriter(formatter, System.err)

    new Output(outWriter, errWriter)
  }
}
