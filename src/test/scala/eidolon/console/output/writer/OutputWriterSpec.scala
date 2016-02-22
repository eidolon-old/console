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

import eidolon.console.output.formatter.OutputFormatter
import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.mock.MockitoSugar

/**
 * OutputWriterSpec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class OutputWriterSpec extends FunSpec with BeforeAndAfter with MockitoSugar{
  var formatter: OutputFormatter = _

  before {
    formatter = mock[OutputFormatter]
  }

  describe("eidolon.console.output.writer.OutputWriter") {
    describe("isQuiet()") {
      it("should return true if the OutputWriter's verbosity is quiet") {
        val writer = new MockOutputWriter(formatter, OutputWriter.VerbosityQuiet)

        assert(writer.isQuiet)
      }

      it("should return true if the OutputWriter's verbosity is not quiet") {
        val writer = new MockOutputWriter(formatter, OutputWriter.VerbosityNormal)

        assert(writer.isQuiet)
      }
    }

    describe("isVerbose()") {
      it("should return true if the OutputWriter's verbosity is verbose") {
        val writer = new MockOutputWriter(formatter, OutputWriter.VerbosityVerbose)

        assert(writer.isVerbose)
      }

      it("should return false if the OutputWriter's verbosity is  ") {
        pending
      }
    }
  }

  private class MockOutputWriter(
      override val formatter: OutputFormatter,
      override val verbosity: Int)
    extends OutputWriter {

    /**
     * @inheritdoc
     */
    override def write(message: String, mode: Int, verbosity: Int): Unit = {}

    /**
     * @inheritdoc
     */
    override def writeln(message: String, mode: Int, verbosity: Int): Unit = {}
  }
}
