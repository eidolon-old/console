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
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.definition.parameter.{InputOption, InputArgument}
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * InputDefinitionDescriptor Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputDefinitionDescriptorSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var application: Application = _
  var inputArgumentDescriptor: InputArgumentDescriptor = _
  var inputOptionDescriptor: InputOptionDescriptor = _

  before {
    application = mock[Application]
    inputArgumentDescriptor = mock[InputArgumentDescriptor]
    inputOptionDescriptor = mock[InputOptionDescriptor]
  }

  describe("eidolon.console.descriptor.InputDefinitionDescriptor") {
    describe("describe()") {
      it("should include a section for arguments") {
        val descriptor = new InputDefinitionDescriptor(inputArgumentDescriptor, inputOptionDescriptor)
        val argument = new InputArgument("foo")
        val definition = new InputDefinition(arguments = List(argument))

        val result = descriptor.describe(application, definition, definition)

        assert(result.contains("Arguments:"))
      }

      it("should describe the definition's arguments") {
        val descriptor = new InputDefinitionDescriptor(inputArgumentDescriptor, inputOptionDescriptor)
        val argument = new InputArgument("foo")
        val definition = new InputDefinition(arguments = List(argument))

        val result = descriptor.describe(application, definition, definition)

        verify(inputArgumentDescriptor)
          .describe(any[Application](), any[InputDefinition](), any[InputArgument]())
      }

      it("should include a section for options") {
        val descriptor = new InputDefinitionDescriptor(inputArgumentDescriptor, inputOptionDescriptor)
        val option = new InputOption("foo")
        val definition = new InputDefinition(options = List(option))

        val result = descriptor.describe(application, definition, definition)

        assert(result.contains("Options:"))
      }

      it("should describe the definition's options") {
        val descriptor = new InputDefinitionDescriptor(inputArgumentDescriptor, inputOptionDescriptor)
        val option = new InputOption("foo")
        val definition = new InputDefinition(options = List(option))

        val result = descriptor.describe(application, definition, definition)

        verify(inputOptionDescriptor)
          .describe(any[Application](), any[InputDefinition](), any[InputOption]())
      }

      it("should be able to include both a section for arguments and options simultaneously") {
        val descriptor = new InputDefinitionDescriptor(inputArgumentDescriptor, inputOptionDescriptor)
        val argument = new InputArgument("foo")
        val option = new InputOption("foo")
        val definition = new InputDefinition(arguments = List(argument), options = List(option))

        val result = descriptor.describe(application, definition, definition)

        assert(result.contains("Arguments:"))
        assert(result.contains("Options:"))
      }

      it("should be able to describe both the definition's arguments and options simultaneously") {
        val descriptor = new InputDefinitionDescriptor(inputArgumentDescriptor, inputOptionDescriptor)
        val argument = new InputArgument("foo")
        val option = new InputOption("foo")
        val definition = new InputDefinition(arguments = List(argument), options = List(option))

        val result = descriptor.describe(application, definition, definition)

        verify(inputArgumentDescriptor)
          .describe(any[Application](), any[InputDefinition](), any[InputArgument]())

        verify(inputOptionDescriptor)
          .describe(any[Application](), any[InputDefinition](), any[InputOption]())
      }
    }
  }
}
