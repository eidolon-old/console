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
import java.io.PrintStream
import org.mockito.invocation.InvocationOnMock
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.stubbing.Answer
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * OutputSpec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class OutputSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var formatter: OutputFormatter = _
  var stream: PrintStream = _

  before {
    formatter = mock[OutputFormatter]
    stream = mock[PrintStream]

    when(formatter.format(anyString(), anyInt())).thenAnswer(new Answer[String] {
      override def answer(invocation: InvocationOnMock): String = {
        invocation.getArgumentAt(0, classOf[String]) + invocation.getArgumentAt(1, classOf[String])
      }
    })
  }

  describe("eidolon.console.output.Output") {
    describe("isQuiet()") {
      it("should return true if the output's verbosity is quiet") {
        val output = new Output(stream, stream, formatter, Output.VerbosityQuiet)

        assert(output.isQuiet)
      }

      it("should return false if the output's verbosity is not quiet") {
        val output = new Output(stream, stream, formatter, Output.VerbosityVeryVerbose)

        assert(!output.isQuiet)
      }
    }

    describe("hasQuiet()") {
      it("should return true if the output's verbosity is quiet") {
        val output = new Output(stream, stream, formatter, Output.VerbosityQuiet)

        assert(output.hasQuiet)
      }

      it("should return true if the output's verbosity includes quiet") {
        val output = new Output(stream, stream, formatter, Output.VerbosityVeryVerbose)

        assert(output.hasQuiet)
      }
    }

    describe("isNormal()") {
      it("should return true if the output's verbosity is normal") {
        val output = new Output(stream, stream, formatter, Output.VerbosityNormal)

        assert(output.isNormal)
      }

      it("should return false if the output's verbosity is not normal") {
        val output = new Output(stream, stream, formatter, Output.VerbosityVeryVerbose)

        assert(!output.isNormal)
      }
    }

    describe("hasNormal()") {
      it("should return true if the output's verbosity is normal") {
        val output = new Output(stream, stream, formatter, Output.VerbosityNormal)

        assert(output.hasNormal)
      }

      it("should return true if the output's verbosity includes normal") {
        val output = new Output(stream, stream, formatter, Output.VerbosityDebug)

        assert(output.hasNormal)
      }

      it("should return false if the output's verbosity does not include normal") {
        val output = new Output(stream, stream, formatter, Output.VerbosityQuiet)

        assert(!output.hasNormal)
      }
    }

    describe("isVerbose()") {
      it("should return true if the output's verbosity is verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityVerbose)

        assert(output.isVerbose)
      }

      it("should return false if the output's verbosity is not verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityVeryVerbose)

        assert(!output.isVerbose)
      }
    }

    describe("hasVerbose()") {
      it("should return true if the output's verbosity is verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityVerbose)

        assert(output.hasVerbose)
      }

      it("should return true if the output's verbosity includes verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityDebug)

        assert(output.hasVerbose)
      }

      it("should return false if the output's verbosity does not include verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityQuiet)

        assert(!output.hasVerbose)
      }
    }

    describe("isVeryVerbose()") {
      it("should return true if the output's verbosity is very verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityVeryVerbose)

        assert(output.isVeryVerbose)
      }

      it("should return false if the output's verbosity is not very verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityQuiet)

        assert(!output.isVeryVerbose)
      }
    }

    describe("hasVeryVerbose()") {
      it("should return true if the output's verbosity is very verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityVeryVerbose)

        assert(output.hasVeryVerbose)
      }

      it("should return true if the output's verbosity includes very verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityDebug)

        assert(output.hasVerbose)
      }

      it("should return false if the output's verbosity does not include very verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityQuiet)

        assert(!output.hasVeryVerbose)
      }
    }

    describe("isDebug()") {
      it("should return true if the output's verbosity is debug") {
        val output = new Output(stream, stream, formatter, Output.VerbosityDebug)

        assert(output.isDebug)
      }

      it("should return false if the output's verbosity is not debug") {
        val output = new Output(stream, stream, formatter, Output.VerbosityVeryVerbose)

        assert(!output.isDebug)
      }
    }

    describe("hasDebug()") {
      it("should return true if the output's verbosity is very verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityDebug)

        assert(output.hasDebug)
      }

      it("should return false if the output's verbosity does not include very verbose") {
        val output = new Output(stream, stream, formatter, Output.VerbosityQuiet)

        assert(!output.hasDebug)
      }
    }

    describe("write()") {
      it("should write to the stream") {
        val output = new Output(stream, stream, formatter, Output.VerbosityNormal)
        val mode = Output.ModeNormal

        output.write("test", mode, Output.VerbosityNormal)

        verify(stream).print("test" + mode)
      }

      it("should not write to the stream if the verbosity of the output is less than the message") {
        val output = new Output(stream, stream, formatter, Output.VerbosityNormal)
        val mode = Output.ModeNormal

        output.write("test", mode, Output.VerbosityDebug)

        verify(stream, never()).print(anyString())
      }

      it("should format output") {
        val output = new Output(stream, stream, formatter, Output.VerbosityNormal)
        val mode1 = Output.ModeNormal
        val mode2 = Output.ModePlain
        val mode3 = Output.ModeRaw

        output.write("test", mode1)

        verify(stream).print("test" + mode1)

        output.write("test", mode2)

        verify(stream).print("test" + mode2)

        output.write("test", mode3)

        verify(stream).print("test" + mode3)
      }

      it("should write to the stream passed, if one is given") {
        val localStream = mock[PrintStream]
        val output = new Output(stream, stream, formatter, Output.VerbosityNormal)

        output.write("test", stream = localStream)

        verify(localStream).print(anyString())
        verify(stream, never()).print(anyString())
      }
    }

    describe("writeln()") {
      it("should write to the stream with a newline appended") {
        val output = new Output(stream, stream, formatter, Output.VerbosityNormal)
        val mode = Output.ModeNormal

        output.writeln("test", mode, Output.VerbosityNormal)

        verify(stream).print("test" + mode + sys.props("line.separator"))
      }

      it("should not write to the stream if the verbosity of the output is less than the message") {
        val output = new Output(stream, stream, formatter, Output.VerbosityNormal)
        val mode = Output.ModeNormal

        output.writeln("test", mode, Output.VerbosityDebug)

        verify(stream, never()).print(anyString())
      }

      it("should format output") {
        val output = new Output(stream, stream, formatter, Output.VerbosityNormal)
        val mode1 = Output.ModeNormal
        val mode2 = Output.ModePlain
        val mode3 = Output.ModeRaw

        output.writeln("test", mode1)

        verify(stream).print("test" + mode1 + sys.props("line.separator"))

        output.writeln("test", mode2)

        verify(stream).print("test" + mode2 + sys.props("line.separator"))

        output.writeln("test", mode3)

        verify(stream).print("test" + mode3 + sys.props("line.separator"))
      }

      it("should write to the stream passed, if one is given") {
        val localStream = mock[PrintStream]
        val output = new Output(stream, stream, formatter, Output.VerbosityNormal)

        output.writeln("test", stream = localStream)

        verify(localStream).print(anyString())
        verify(stream, never()).print(anyString())
      }
    }
  }
}
