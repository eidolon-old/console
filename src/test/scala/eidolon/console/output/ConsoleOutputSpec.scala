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

import java.io.ByteArrayOutputStream

import eidolon.console.output.formatter.OutputFormatter
import org.mockito.AdditionalAnswers._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * ConsoleOutputSpec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleOutputSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  private var formatter: OutputFormatter = _
  private var errOutput: Output = _

  before {
    formatter = mock[OutputFormatter]
    errOutput = mock[Output]

    when(formatter.format(anyString(), anyInt())).thenAnswer(returnsFirstArg())
  }

  describe("eidolon.console.output.ConsoleOutput") {
    describe("isQuiet()") {
      it("should return true if the verbosity is quiet") {
        val output = new ConsoleOutput(formatter, errOutput, Output.VerbosityQuiet)

        assert(output.isQuiet)
      }

      it("should return false if the verbosity is not quiet") {
        val output = new ConsoleOutput(formatter, errOutput, Output.VerbosityNormal)

        assert(!output.isQuiet)
      }
    }

    describe("isVerbose()") {
      it("should return true if the verbosity is verbose") {
        val output = new ConsoleOutput(formatter, errOutput, Output.VerbosityVerbose)

        assert(output.isVerbose)
      }

      it("should return false if the verbosity is not verbose") {
        val output = new ConsoleOutput(formatter, errOutput, Output.VerbosityNormal)

        assert(!output.isVerbose)
      }
    }

    describe("isVeryVerbose()") {
      it("should return true if the verbosity is very verbose") {
        val output = new ConsoleOutput(formatter, errOutput, Output.VerbosityVeryVerbose)

        assert(output.isVeryVerbose)
      }

      it("should return false if the verbosity is not very verbose") {
        val output = new ConsoleOutput(formatter, errOutput, Output.VerbosityNormal)

        assert(!output.isVeryVerbose)
      }
    }

    describe("isDebug()") {
      it("should return true if the verbosity is debug") {
        val output = new ConsoleOutput(formatter, errOutput, Output.VerbosityDebug)

        assert(output.isDebug)
      }

      it("should return false if the verbosity is not debug") {
        val output = new ConsoleOutput(formatter, errOutput, Output.VerbosityNormal)

        assert(!output.isDebug)
      }
    }

    describe("writeln()") {
      it("should write a message") {
        val output = new ConsoleOutput(formatter, errOutput)
        val stream = new ByteArrayOutputStream()
        val message = "Hello world"

        Console.withOut(stream) {
          output.writeln(message)
        }

        assert(stream.toString.contains(message))
      }

      it("should write a message with a new line at the end") {
        val output = new ConsoleOutput(formatter, errOutput)
        val stream = new ByteArrayOutputStream()
        val message = "Hello world"

        Console.withOut(stream) {
          output.writeln(message)
        }

        assert(!message.contains(sys.props("line.separator")))
        assert(stream.toString.endsWith(sys.props("line.separator")))
      }
    }

    describe("write()") {
      it("should write a message") {
        val output = new ConsoleOutput(formatter, errOutput)
        val stream = new ByteArrayOutputStream()
        val message = "Hello world"

        Console.withOut(stream) {
          output.write(message)
        }

        assert(stream.toString.contains(message))
      }
    }
  }
}
