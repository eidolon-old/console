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

package eidolon.console.output.writer.factory

import java.io.PrintStream

import eidolon.console.output.formatter.OutputFormatter
import eidolon.console.output.writer.{PrintStreamOutputWriter, OutputWriter}

/**
 * PrintStreamOutputWriter Factory
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class PrintStreamOutputWriterFactory(
    formatter: OutputFormatter,
    stream: PrintStream)
  extends OutputWriterFactory {

  /**
   * @inheritdoc
   */
  override def build(verbosity: Int): OutputWriter = {
    new PrintStreamOutputWriter(formatter, stream, verbosity)
  }
}
