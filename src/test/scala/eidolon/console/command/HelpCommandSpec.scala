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

package eidolon.console.command

import eidolon.console.Application
import eidolon.console.descriptor.CommandDescriptor
import eidolon.console.dialog.Dialog
import eidolon.console.input.Input
import eidolon.console.input.definition.InputDefinition
import eidolon.console.output.Output
import eidolon.console.output.writer.OutputWriter
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.mock.MockitoSugar

/**
 * HelpCommand Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class HelpCommandSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var baseApplication: Application = _
  var buffer: StringBuilder = _
  var descriptor: CommandDescriptor = _
  var input: Input = _
  var output: Output = _
  var dialog: Dialog = _
  var writer: OutputWriter = _

  before {
    baseApplication = Application("test/console", "0.0.0")
    buffer = new StringBuilder()
    descriptor = mock[CommandDescriptor]
    input = mock[Input]
    output = mock[Output]
    dialog = mock[Dialog]
    writer = mock[OutputWriter]

    when(input.arguments).thenReturn(Map(
      "command_name" -> "test"
    ))

    when(output.out).thenReturn(writer)

    when(writer.writeln(anyString(), anyInt(), anyInt()))
      .thenAnswer(new Answer[Unit] {
        override def answer(invocation: InvocationOnMock): Unit = {
          buffer.append(invocation.getArgumentAt(0, classOf[String]))
        }
      })
  }

  describe("eidolon.console.command.HelpCommand") {
    describe("execute()") {
      it("should describe the given command if it exists") {
        val application = baseApplication
          .withCommand(new AmbiguousCommand {
            override val name: String = "test"
            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
          })

        val command = new HelpCommand(application, descriptor)

        command.run(input, output, dialog)

        val result = buffer.toString()

        verify(descriptor).describe(any[Application](), any[InputDefinition](), any[Command[_]]())
      }

      it("should have an exit status code of 0 if the given command exists") {
        val application = baseApplication
          .withCommand(new AmbiguousCommand {
            override val name: String = "test"
            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
          })

        val command = new HelpCommand(application, descriptor)
        val result = command.run(input, output, dialog)

        assert(result == 0)
      }

      it("should show an error message if the given command doesn't exist") {
        val command = new HelpCommand(baseApplication, descriptor)

        command.run(input, output, dialog)

        val result = buffer.toString()

        assert(result.contains("Command 'test' does not exist."))
      }

      it("should have an exit status code of 1 if the given command doesn't exist") {
        val command = new HelpCommand(baseApplication, descriptor)
        val result = command.run(input, output, dialog)

        assert(result == 1)
      }
    }
  }
}
