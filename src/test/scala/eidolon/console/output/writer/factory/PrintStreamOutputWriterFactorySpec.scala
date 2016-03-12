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
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * PrintStreamOutputWriterFactory Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class PrintStreamOutputWriterFactorySpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var formatter: OutputFormatter = _
  var stream: PrintStream = _

  before {
    formatter = mock[OutputFormatter]
    stream = mock[PrintStream]
  }

  describe("eidolon.console.output.writer.factory.PrintStreamOutputWriterFactory") {
    describe("build()") {
      it("should return a PrintStreamOutputWriter") {
        val factory = new PrintStreamOutputWriterFactory(formatter, stream)
        val result = factory.build(OutputWriter.VerbosityNormal)

        assert(result.isInstanceOf[PrintStreamOutputWriter])
      }
    }
  }
}
