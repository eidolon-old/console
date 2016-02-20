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

import java.io.{ByteArrayOutputStream, OutputStream}

import eidolon.console.output.formatter.OutputFormatter
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * ConsoleErrorOutputSpec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleErrorOutputSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  private var formatter: OutputFormatter = _
  private var outputStream: OutputStream = _
  private var message: String = _
  private var lineSeparator: String = _

  before {
    formatter = mock[OutputFormatter]
    outputStream = new ByteArrayOutputStream()
    message = "Hello world"
    lineSeparator = sys.props("line.separator")

    when(formatter.format(Matchers.eq(message), Matchers.eq(Output.OutputNormal)))
      .thenReturn(message.concat("normal"))

    when(formatter.format(Matchers.eq(message), Matchers.eq(Output.OutputPlain)))
      .thenReturn(message.concat("plain"))

    when(formatter.format(Matchers.eq(message), Matchers.eq(Output.OutputRaw)))
      .thenReturn(message.concat("raw"))
  }

  describe("eidolon.console.output.ConsoleOutput") {
    describe("isQuiet()") {
      it("should return true if the verbosity is quiet") {
        val output = new ConsoleErrorOutput(formatter, Output.VerbosityQuiet)

        assert(output.isQuiet)
      }

      it("should return false if the verbosity is not quiet") {
        val output = new ConsoleErrorOutput(formatter, Output.VerbosityNormal)

        assert(!output.isQuiet)
      }
    }

    describe("isVerbose()") {
      it("should return true if the verbosity is verbose") {
        val output = new ConsoleErrorOutput(formatter, Output.VerbosityVerbose)

        assert(output.isVerbose)
      }

      it("should return false if the verbosity is not verbose") {
        val output = new ConsoleErrorOutput(formatter, Output.VerbosityNormal)

        assert(!output.isVerbose)
      }
    }

    describe("isVeryVerbose()") {
      it("should return true if the verbosity is very verbose") {
        val output = new ConsoleErrorOutput(formatter, Output.VerbosityVeryVerbose)

        assert(output.isVeryVerbose)
      }

      it("should return false if the verbosity is not very verbose") {
        val output = new ConsoleErrorOutput(formatter, Output.VerbosityNormal)

        assert(!output.isVeryVerbose)
      }
    }

    describe("isDebug()") {
      it("should return true if the verbosity is debug") {
        val output = new ConsoleErrorOutput(formatter, Output.VerbosityDebug)

        assert(output.isDebug)
      }

      it("should return false if the verbosity is not debug") {
        val output = new ConsoleErrorOutput(formatter, Output.VerbosityNormal)

        assert(!output.isDebug)
      }
    }

    describe("writeln()") {
      it("should write a message") {
        val output = new ConsoleErrorOutput(formatter)

        Console.withErr(outputStream) {
          output.writeln(message)
        }

        assert(outputStream.toString.contains(message))
      }

      it("should write a message with a new line at the end") {
        val output = new ConsoleErrorOutput(formatter)

        Console.withErr(outputStream) {
          output.writeln(message)
        }

        assert(!message.contains(lineSeparator))
        assert(outputStream.toString.endsWith(lineSeparator))
      }

      it("should not write a message if the given verbosity is higher than the output's verbosity") {
        val output = new ConsoleErrorOutput(formatter)
        val verbosity = Output.VerbosityVerbose

        Console.withErr(outputStream) {
          output.writeln(message, verbosity = verbosity)
        }

        assert(outputStream.toString == "")
      }

      it("should use a normal output mode by default") {
        val output = new ConsoleErrorOutput(formatter)

        Console.withErr(outputStream) {
          output.writeln(message)
        }

        assert(outputStream.toString == (message + "normal" + lineSeparator))
      }

      it("should pass the output mode to the formatter") {
        val output = new ConsoleErrorOutput(formatter)

        Console.withErr(outputStream) {
          output.writeln(message, mode = Output.OutputRaw)
        }

        assert(outputStream.toString == (message + "raw" + lineSeparator))
      }
    }

    describe("write()") {
      it("should write a message") {
        val output = new ConsoleErrorOutput(formatter)

        Console.withErr(outputStream) {
          output.write(message)
        }

        assert(outputStream.toString.contains(message))
      }

      it("should not write a new line at the end of output by default") {
        val output = new ConsoleErrorOutput(formatter)

        Console.withErr(outputStream) {
          output.write(message)
        }

        assert(!outputStream.toString.contains(lineSeparator))
      }

      it("should write a message with a new line at the end if newLine is set to true") {
        val output = new ConsoleErrorOutput(formatter)

        Console.withErr(outputStream) {
          output.write(message, newLine = true)
        }

        assert(!message.contains(lineSeparator))
        assert(outputStream.toString.endsWith(lineSeparator))
      }

      it("should not write a message if the given verbosity is higher than the output's verbosity") {
        val output = new ConsoleErrorOutput(formatter)
        val verbosity = Output.VerbosityVerbose

        Console.withErr(outputStream) {
          output.write(message, verbosity = verbosity)
        }

        assert(outputStream.toString == "")
      }

      it("should use a normal output mode by default") {
        val output = new ConsoleErrorOutput(formatter)

        Console.withErr(outputStream) {
          output.write(message)
        }

        assert(outputStream.toString == (message + "normal"))
      }

      it("should pass the output mode to the formatter") {
        val output = new ConsoleErrorOutput(formatter)

        Console.withErr(outputStream) {
          output.write(message, mode = Output.OutputRaw)
        }

        assert(outputStream.toString == (message + "raw"))
      }
    }
  }
}
