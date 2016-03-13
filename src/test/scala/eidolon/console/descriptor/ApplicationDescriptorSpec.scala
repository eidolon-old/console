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

package eidolon.console.descriptor

import eidolon.console.Application
import eidolon.console.command.AmbiguousCommand
import eidolon.console.dialog.Dialog
import eidolon.console.dialog.factory.DialogFactory
import eidolon.console.input.Input
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.definition.parameter.{InputOption, InputArgument}
import eidolon.console.input.factory.InputFactory
import eidolon.console.input.parser.InputParser
import eidolon.console.input.validation.InputValidator
import eidolon.console.output.Output
import eidolon.console.output.factory.OutputFactory
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * ApplicationDescriptor Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ApplicationDescriptorSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var baseApplication: Application = _
  var definition: InputDefinition = _
  var definitionDescriptor: InputDefinitionDescriptor = _

  before {
    baseApplication = new Application(
      "test/console",
      "0.0.0",
      mock[InputParser],
      mock[InputValidator],
      mock[InputFactory],
      mock[OutputFactory],
      mock[DialogFactory]
    )

    definitionDescriptor = mock[InputDefinitionDescriptor]
    definition = new InputDefinition(
      arguments = List(new InputArgument("foo")),
      options = List(new InputOption("bar"))
    )
  }

  describe("eidolon.console.descriptor.ApplicationDescriptor") {
    describe("describe()") {
      it("should include a section for usage") {
        val application = baseApplication
        val descriptor = new ApplicationDescriptor(definitionDescriptor)
        val result = descriptor.describe(application, definition, application)

        assert(result.contains("Usage:"))
      }

      it("should include a section for available commands") {
        val application = baseApplication
        val descriptor = new ApplicationDescriptor(definitionDescriptor)
        val result = descriptor.describe(application, definition, application)

        assert(result.contains("Available commands:"))
      }

      it("should show all commands in the application") {
        val application = baseApplication
          .withCommand(new AmbiguousCommand {
            override val name: String = "foo"
            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
          })
          .withCommand(new AmbiguousCommand {
            override val name: String = "bar:a"
            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
          })
          .withCommand(new AmbiguousCommand {
            override val name: String = "bar:b"
            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
          })
          .withCommand(new AmbiguousCommand {
            override val name: String = "baz:a"
            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
          })

        val descriptor = new ApplicationDescriptor(definitionDescriptor)
        val result = descriptor.describe(application, definition, application)

        assert(result.contains("foo"))
        assert(result.contains("bar:a"))
        assert(result.contains("bar:b"))
        assert(result.contains("baz:a"))
      }
    }
  }
}
