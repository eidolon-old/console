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

package eidolon.console.dialog

import java.io.{OutputStream, InputStream}

import eidolon.console.output.formatter.OutputFormatter
import jline.console.ConsoleReader
import org.mockito.AdditionalAnswers._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * Dialog Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class DialogSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var formatter: OutputFormatter = _
  var reader: ConsoleReader = _

  before {
    formatter = mock[OutputFormatter]
    reader = mock[ConsoleReader]

    when(formatter.format(anyString(), anyInt()))
      .thenAnswer(returnsFirstArg())

    when(reader.readLine(anyString()))
      .thenReturn("response")

    when(reader.readLine(anyString(), anyChar()))
      .thenReturn("response")
  }

  describe("eidolon.console.dialog.Dialog") {
    describe("ask()") {
      it("should prompt the user") {
        val dialog = new Dialog(formatter, reader)

        dialog.ask("foo bar baz?")

        verify(reader)
          .readLine("<question>foo bar baz?</question> ")
      }

      it("should prompt the user with a default value") {
        val dialog = new Dialog(formatter, reader)

        dialog.ask("foo bar baz?", Some("default"))

        verify(reader)
          .readLine("<question>foo bar baz?</question> [<info>default</info>] ")
      }

      it("should return the user input if it is given") {
        val dialog = new Dialog(formatter, reader)
        val result = dialog.ask("foo bar baz?")

        assert(result == "response")
      }

      it("should return the default if one is set and no input is given") {
        val reader = mock[ConsoleReader]

        when(reader.readLine(anyString()))
          .thenReturn("")

        val dialog = new Dialog(formatter, reader)
        val result = dialog.ask("foo bar baz?", Some("default"))

        assert(result == "default")
      }

      it("should return an empty string if no default is set and no input is given") {
        val reader = mock[ConsoleReader]

        when(reader.readLine(anyString()))
          .thenReturn("")

        val dialog = new Dialog(formatter, reader)
        val result = dialog.ask("foo bar baz?")

        assert(result == "")
      }
    }

    describe("askConfirmation()") {
      it("should prompt the user") {
        val dialog = new Dialog(formatter, reader)

        dialog.askConfirmation("foo bar baz?")

        verify(reader)
          .readLine("<question>foo bar baz?</question> [<info>yN</info>] ")
      }

      it("should indicate that the default result be true if the default is set to true") {
        val dialog = new Dialog(formatter, reader)

        dialog.askConfirmation("foo bar baz?", default = true)

        verify(reader)
          .readLine("<question>foo bar baz?</question> [<info>Yn</info>] ")
      }

      it("should indicate that the default result be false if the default is set to false") {
        val dialog = new Dialog(formatter, reader)

        dialog.askConfirmation("foo bar baz?", default = false)

        verify(reader)
          .readLine("<question>foo bar baz?</question> [<info>yN</info>] ")
      }

      it("should return true if the response is 'y'") {
        val reader = mock[ConsoleReader]

        when(reader.readLine(anyString()))
          .thenReturn("y")

        val dialog = new Dialog(formatter, reader)
        val result = dialog.askConfirmation("foo bar baz?")

        assert(result)
      }

      it("should return false if the response is not 'y' or ''") {
        val reader = mock[ConsoleReader]

        when(reader.readLine(anyString()))
          .thenReturn("test")

        val dialog = new Dialog(formatter, reader)
        val result = dialog.askConfirmation("foo bar baz?")

        assert(!result)
      }

      it("should return true if the default is set to true and no input is given") {
        val reader = mock[ConsoleReader]

        when(reader.readLine(anyString()))
          .thenReturn("")

        val dialog = new Dialog(formatter, reader)
        val result = dialog.askConfirmation("foo bar baz?", default = true)

        assert(result)
      }

      it("should return true if the default is set to true and 'y' is given as input") {
        val reader = mock[ConsoleReader]

        when(reader.readLine(anyString()))
          .thenReturn("y")

        val dialog = new Dialog(formatter, reader)
        val result = dialog.askConfirmation("foo bar baz?", default = true)

        assert(result)
      }

      it("should return false if the default is set to false and no input is given") {
        val reader = mock[ConsoleReader]

        when(reader.readLine(anyString()))
          .thenReturn("")

        val dialog = new Dialog(formatter, reader)
        val result = dialog.askConfirmation("foo bar baz?", default = false)

        assert(!result)
      }
    }

    describe("askSensitive()") {
      it("should prompt the user") {
        val dialog = new Dialog(formatter, reader)

        dialog.askSensitive("foo bar baz?")

        verify(reader)
          .readLine("<question>foo bar baz?</question> ", new Character('*'))
      }

      it("should prompt the user with a default value") {
        val dialog = new Dialog(formatter, reader)

        dialog.askSensitive("foo bar baz?", Some("default"))

        verify(reader)
          .readLine("<question>foo bar baz?</question> [<info>default</info>] ", new Character('*'))
      }

      it("should return the user input if it is given") {
        val dialog = new Dialog(formatter, reader)
        val result = dialog.ask("foo bar baz?")

        assert(result == "response")
      }

      it("should return the default if one is set and no input is given") {
        val reader = mock[ConsoleReader]

        when(reader.readLine(anyString(), anyChar()))
          .thenReturn("")

        val dialog = new Dialog(formatter, reader)
        val result = dialog.askSensitive("foo bar baz?", Some("default"))

        assert(result == "default")
      }

      it("should return an empty string if no default is set and no input is given") {
        val reader = mock[ConsoleReader]

        when(reader.readLine(anyString(), anyChar()))
          .thenReturn("")

        val dialog = new Dialog(formatter, reader)
        val result = dialog.askSensitive("foo bar baz?")

        assert(result == "")
      }
    }
  }
}
