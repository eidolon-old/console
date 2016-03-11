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
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * PrintStreamOutputWriter Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class PrintStreamOutputWriterSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
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

  describe("eidolon.console.output.writer.PrintStreamOutputWriter") {
    describe("isQuiet()") {
      it("should return true if the writer's verbosity is quiet") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityQuiet)

        assert(writer.isQuiet)
      }

      it("should return false if the writer's verbosity is not quiet") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityVeryVerbose)

        assert(!writer.isQuiet)
      }
    }

    describe("hasQuiet()") {
      it("should return true if the writer's verbosity is quiet") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityQuiet)

        assert(writer.hasQuiet)
      }

      it("should return true if the writer's verbosity includes quiet") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityVeryVerbose)

        assert(writer.hasQuiet)
      }
    }

    describe("isNormal()") {
      it("should return true if the writer's verbosity is normal") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityNormal)

        assert(writer.isNormal)
      }

      it("should return false if the writer's verbosity is not normal") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityVeryVerbose)

        assert(!writer.isNormal)
      }
    }

    describe("hasNormal()") {
      it("should return true if the writer's verbosity is normal") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityNormal)

        assert(writer.hasNormal)
      }

      it("should return true if the writer's verbosity includes normal") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityDebug)

        assert(writer.hasNormal)
      }

      it("should return false if the writer's verbosity does not include normal") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityQuiet)

        assert(!writer.hasNormal)
      }
    }

    describe("isVerbose()") {
      it("should return true if the writer's verbosity is verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityVerbose)

        assert(writer.isVerbose)
      }

      it("should return false if the writer's verbosity is not verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityVeryVerbose)

        assert(!writer.isVerbose)
      }
    }

    describe("hasVerbose()") {
      it("should return true if the writer's verbosity is verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityVerbose)

        assert(writer.hasVerbose)
      }

      it("should return true if the writer's verbosity includes verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityDebug)

        assert(writer.hasVerbose)
      }

      it("should return false if the writer's verbosity does not include verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityQuiet)

        assert(!writer.hasVerbose)
      }
    }

    describe("isVeryVerbose()") {
      it("should return true if the writer's verbosity is very verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityVeryVerbose)

        assert(writer.isVeryVerbose)
      }

      it("should return false if the writer's verbosity is not very verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityQuiet)

        assert(!writer.isVeryVerbose)
      }
    }

    describe("hasVeryVerbose()") {
      it("should return true if the writer's verbosity is very verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityVeryVerbose)

        assert(writer.hasVeryVerbose)
      }

      it("should return true if the writer's verbosity includes very verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityDebug)

        assert(writer.hasVerbose)
      }

      it("should return false if the writer's verbosity does not include very verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityQuiet)

        assert(!writer.hasVeryVerbose)
      }
    }

    describe("isDebug()") {
      it("should return true if the writer's verbosity is debug") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityDebug)

        assert(writer.isDebug)
      }

      it("should return false if the writer's verbosity is not debug") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityVeryVerbose)

        assert(!writer.isDebug)
      }
    }

    describe("hasDebug()") {
      it("should return true if the writer's verbosity is very verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityDebug)

        assert(writer.hasDebug)
      }

      it("should return false if the writer's verbosity does not include very verbose") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityQuiet)

        assert(!writer.hasDebug)
      }
    }

    describe("write()") {
      it("should write to the stream") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityNormal)
        val mode = OutputWriter.ModeNormal

        writer.write("test", mode, OutputWriter.VerbosityNormal)

        verify(stream).print("test" + mode)
      }

      it("should not write to the stream if the verbosity of the writer is less than the message") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityNormal)
        val mode = OutputWriter.ModeNormal

        writer.write("test", mode, OutputWriter.VerbosityDebug)

        verify(stream, never()).print(anyString())
      }

      it("should format output") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityNormal)
        val mode1 = OutputWriter.ModeNormal
        val mode2 = OutputWriter.ModePlain
        val mode3 = OutputWriter.ModeRaw

        writer.write("test", mode1)

        verify(stream).print("test" + mode1)

        writer.write("test", mode2)

        verify(stream).print("test" + mode2)

        writer.write("test", mode3)

        verify(stream).print("test" + mode3)
      }
    }

    describe("writeln()") {
      it("should write to the stream with a newline appended") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityNormal)
        val mode = OutputWriter.ModeNormal

        writer.writeln("test", mode, OutputWriter.VerbosityNormal)

        verify(stream).print("test" + mode + sys.props("line.separator"))
      }

      it("should not write to the stream if the verbosity of the writer is less than the message") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityNormal)
        val mode = OutputWriter.ModeNormal

        writer.writeln("test", mode, OutputWriter.VerbosityDebug)

        verify(stream, never()).print(anyString())
      }

      it("should format output") {
        val writer = new PrintStreamOutputWriter(formatter, stream, OutputWriter.VerbosityNormal)
        val mode1 = OutputWriter.ModeNormal
        val mode2 = OutputWriter.ModePlain
        val mode3 = OutputWriter.ModeRaw

        writer.writeln("test", mode1)

        verify(stream).print("test" + mode1 + sys.props("line.separator"))

        writer.writeln("test", mode2)

        verify(stream).print("test" + mode2 + sys.props("line.separator"))

        writer.writeln("test", mode3)

        verify(stream).print("test" + mode3 + sys.props("line.separator"))
      }
    }
  }
}
