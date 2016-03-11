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

package eidolon.console.output.formatter

import eidolon.console.output.formatter.style.{OutputFormatterStyleGroup, OutputFormatterStyle}
import eidolon.console.output.writer.OutputWriter
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.mock.MockitoSugar

/**
 * ConsoleOutputFormatter Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleOutputFormatterSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var styles: OutputFormatterStyleGroup = _

  before {
    val style = mock[OutputFormatterStyle]

    when(style.name).thenReturn("test")
    when(style.applyStyle(anyString())).thenAnswer(new Answer[String] {
      override def answer(invocation: InvocationOnMock): String = {
        "(ss)" + invocation.getArgumentAt(0, classOf[String]) + "(se)"
      }
    })

    styles = new OutputFormatterStyleGroup(List(style))
  }

  describe("eidolon.console.output.formatter.ConsoleOutputFormatter") {
    describe("format()") {
      it("should be able to output messages including formatting strings") {
        val formatter = new ConsoleOutputFormatter(styles)
        val message = "<test>foo bar baz</test>"

        val result = formatter.format(message, OutputWriter.ModeRaw)

        assert(result == message)
      }

      it("should be able to output messages without styles applied") {
        val formatter = new ConsoleOutputFormatter(styles)
        val message = "<test>foo bar baz</test>"

        val result = formatter.format(message, OutputWriter.ModePlain)

        assert(result == "foo bar baz")
      }

      it("should be able to output messages with styles applied") {
        val formatter = new ConsoleOutputFormatter(styles)
        val message = "<test>foo bar baz</test>"

        val result = formatter.format(message, OutputWriter.ModeNormal)

        assert(result == "(ss)foo bar baz(se)")
      }

      it("should be able to output messages with nested styles applied") {
        val formatter = new ConsoleOutputFormatter(styles)
        val message = "<test>foo <test>bar</test> baz <test>qux</test></test>"

        val result = formatter.format(message, OutputWriter.ModeNormal)

        assert(result == "(ss)foo (ss)bar(se) baz (ss)qux(se)(se)")
      }
    }

    describe("withStyle()") {
      it("should create a new instance of the formatter with the given style added") {
        val formatter = new ConsoleOutputFormatter(new OutputFormatterStyleGroup())
        val style1 = mock[OutputFormatterStyle]

        when(style1.name).thenReturn("s1")

        val result = formatter.withStyle(style1)

        assert(formatter != result)
        assert(result.styles.styles.contains(style1))
      }
    }

    describe("withStyles()") {
      it("should create a new instance of the formatter with the given styles added") {
        val formatter = new ConsoleOutputFormatter(new OutputFormatterStyleGroup())
        val style1 = mock[OutputFormatterStyle]
        val style2 = mock[OutputFormatterStyle]

        when(style1.name).thenReturn("s1")
        when(style2.name).thenReturn("s2")

        val result = formatter.withStyles(List(style1, style2))

        assert(formatter != result)
        assert(result.styles.styles.contains(style1))
        assert(result.styles.styles.contains(style2))
      }
    }
  }
}
