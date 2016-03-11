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

package eidolon.console

import eidolon.console.dialog.Dialog
import eidolon.console.dialog.factory.DialogFactory
import eidolon.console.input.Input
import eidolon.console.input.factory.InputFactory
import eidolon.console.input.parser.InputParser
import eidolon.console.input.validation.InputValidator
import eidolon.console.output.Output
import eidolon.console.output.factory.OutputFactory
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * ApplicationSpec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ApplicationSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  val name: String = "eidolon/console"
  val version: String = "1.2.3-test"
  var outputBuffer: StringBuilder = _
  var inputParser: InputParser = _
  var inputValidator: InputValidator = _
  var inputFactory: InputFactory = _
  var outputFactory: OutputFactory = _
  var dialogFactory: DialogFactory = _
  var input: Input = _
  var output: Output = _
  var dialog: Dialog = _

  before {
    outputBuffer = new StringBuilder()
    inputParser = mock[InputParser]
    inputValidator = mock[InputValidator]
    inputFactory = mock[InputFactory]
    outputFactory = mock[OutputFactory]
    dialogFactory = mock[DialogFactory]
    input = mock[Input]
    output = mock[Output]
    dialog = mock[Dialog]

    when(inputFactory.build()).thenReturn(input)
    when(outputFactory.build()).thenReturn(output)
    when(dialogFactory.build()).thenReturn(dialog)
  }

  describe("eidolon.console.Application") {
    describe("run()") {

    }

    describe("withCommand()") {
      it("should add a command to the application") {
        pending
      }
    }

    describe("withCommands()") {
      it("should add several commands to the application") {
        pending
      }
    }
  }
}
