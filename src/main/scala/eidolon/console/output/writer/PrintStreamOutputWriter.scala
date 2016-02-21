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

package eidolon.console.output.writer

import java.io.PrintStream

import eidolon.console.output.formatter.OutputFormatter

/**
 * PrintStream Output Writer
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
final class PrintStreamOutputWriter (
    override val formatter: OutputFormatter,
    val stream: PrintStream,
    override val verbosity: Int = OutputWriter.VerbosityNormal)
  extends OutputWriter {

  /**
   * @inheritdoc
   */
  override def write(
      message: String,
      mode: Int = OutputWriter.ModeNormal,
      verbosity: Int = OutputWriter.VerbosityNormal)
    : Unit = {

    if (this.verbosity <= verbosity) {
      stream.print(formatter.format(message, mode))
    }
  }

  /**
   * @inheritdoc
   */
  override def writeln(
      message: String,
      mode: Int = OutputWriter.ModeNormal,
      verbosity: Int = OutputWriter.VerbosityNormal)
    : Unit = {

    if (this.verbosity <= verbosity) {
      stream.println(formatter.format(message, mode))
    }
  }
}
