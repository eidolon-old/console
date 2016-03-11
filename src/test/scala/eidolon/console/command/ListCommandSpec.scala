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
import eidolon.console.descriptor.ApplicationDescriptor
import eidolon.console.dialog.Dialog
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.Input
import eidolon.console.output.Output
import eidolon.console.output.writer.OutputWriter
import org.mockito.invocation.InvocationOnMock
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.stubbing.Answer
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * ListCommand Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ListCommandSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var application: Application = _
  var buffer: StringBuilder = _
  var descriptor: ApplicationDescriptor = _
  var input: Input = _
  var output: Output = _
  var dialog: Dialog = _
  var writer: OutputWriter = _

  before {
    application = Application("test/console", "0.0.0")
    buffer = new StringBuilder()
    descriptor = mock[ApplicationDescriptor]
    input = mock[Input]
    output = mock[Output]
    dialog = mock[Dialog]
    writer = mock[OutputWriter]

    when(output.out).thenReturn(writer)

    when(writer.writeln(anyString(), anyInt(), anyInt()))
      .thenAnswer(new Answer[Unit] {
        override def answer(invocation: InvocationOnMock): Unit = {
          buffer.append(invocation.getArgumentAt(0, classOf[String]))
        }
      })
  }

  describe("eidolon.console.command.ListCommand") {
    describe("execute()") {
      it("should describe the application") {
        val command = new ListCommand(application, descriptor)

        command.execute(input, output, dialog)

        val result = buffer.toString()

        verify(descriptor).describe(any[Application](), any[InputDefinition](), any[Application]())
      }
    }
  }
}
