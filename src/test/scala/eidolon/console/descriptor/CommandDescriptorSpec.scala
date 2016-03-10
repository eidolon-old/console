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
import eidolon.console.command.Command
import eidolon.console.dialog.Dialog
import eidolon.console.input.Input
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.definition.parameter.{InputOption, InputArgument}
import eidolon.console.output.Output
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * CommandDescriptor Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class CommandDescriptorSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var application: Application = _
  var definition: InputDefinition = _
  var definitionDescriptor: InputDefinitionDescriptor = _

  before {
    application = mock[Application]
    definitionDescriptor = mock[InputDefinitionDescriptor]

    definition = new InputDefinition(
      arguments = List(new InputArgument("foo")),
      options = List(new InputOption("bar"))
    )

    when(application.definition).thenReturn(definition)
  }

  describe("eidolon.console.descriptor.CommandDescriptor") {
    describe("describe()") {
      it("should include a section for usage") {
        val command = new {} with Command {
          override val name = "test"
          override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
        }

        val descriptor = new CommandDescriptor(definitionDescriptor)
        val result = descriptor.describe(application, definition, command)

        assert(result.contains("Usage:"))
      }

      it("should include the command name") {
        val command = new {} with Command {
          override val name = "foobarbaz"
          override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
        }

        val descriptor = new CommandDescriptor(definitionDescriptor)
        val result = descriptor.describe(application, definition, command)

        assert(result.contains("foobarbaz"))
      }

      it("should include the command's alias(es)") {
        val command = new {} with Command {
          override val name = "test"
          override val aliases = List("foo", "bar", "baz")
          override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
        }

        val descriptor = new CommandDescriptor(definitionDescriptor)
        val result = descriptor.describe(application, definition, command)

        assert(result.contains("foo"))
        assert(result.contains("bar"))
        assert(result.contains("baz"))
      }

      it("should include a section for help") {
        val command = new {} with Command {
          override val name = "test"
          override val help = Some("test command help text")
          override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
        }

        val descriptor = new CommandDescriptor(definitionDescriptor)
        val result = descriptor.describe(application, definition, command)

        assert(result.contains("test command help text"))
      }

      it("should describe the definition's arguments") {
        val command = new {} with Command {
          override val name = "test"
          override val definition = new InputDefinition(arguments = List(new InputArgument("foo")))
          override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
        }

        val descriptor = new CommandDescriptor(definitionDescriptor)
        val result = descriptor.describe(application, definition, command)

        verify(definitionDescriptor)
          .describe(any[Application](), any[InputDefinition](), any[InputDefinition]())
      }

      it("should indicate if an argument is optional") {
        val command = new {} with Command {
          override val name = "test"
          override val definition = new InputDefinition(arguments = List(new InputArgument("foo")))
          override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
        }

        val descriptor = new CommandDescriptor(definitionDescriptor)
        val result = descriptor.describe(application, definition, command)

        assert(result.contains("[foo]"))
      }

      it("should indicate if an argument is required") {
        val command = new {} with Command {
          override val name = "test"
          override val definition = new InputDefinition(
            arguments = List(new InputArgument("foo", mode = InputArgument.REQUIRED))
          )

          override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
        }

        val descriptor = new CommandDescriptor(definitionDescriptor)
        val result = descriptor.describe(application, definition, command)

        assert(result.contains("foo"))
      }

      it("should describe the command's options") {
        val command = new {} with Command {
          override val name = "test"
          override val definition = new InputDefinition(options = List(new InputOption("foo")))
          override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
        }

        val descriptor = new CommandDescriptor(definitionDescriptor)
        val result = descriptor.describe(application, definition, command)

        verify(definitionDescriptor)
          .describe(any[Application](), any[InputDefinition](), any[InputDefinition]())
      }
    }
  }
}
