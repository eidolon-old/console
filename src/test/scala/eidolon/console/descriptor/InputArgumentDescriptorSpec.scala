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
import eidolon.console.input.definition.parameter.InputArgument
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * InputArgumentDescriptor Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputArgumentDescriptorSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var application: Application = _

  before {
    application = mock[Application]
  }

  describe("eidolon.console.descriptor.InputArgumentDescriptor") {
    describe("describe()") {
      it("should include the argument's name") {
        val descriptor = new InputArgumentDescriptor()
        val argument = new InputArgument("foo")
        val definition = new InputDefinition(arguments = List(argument))

        val result = descriptor.describe(application, definition, argument)

        assert(result.contains("foo"))
      }

      it("should include the argument's description if one is set") {
        val descriptor = new InputArgumentDescriptor()
        val argument = new InputArgument("foo", description = Some("foo bar baz qux"))
        val definition = new InputDefinition(arguments = List(argument))

        val result = descriptor.describe(application, definition, argument)

        assert(result.contains("foo bar baz qux"))
      }

      it("should describe the argument's default if one is set") {
        val descriptor = new InputArgumentDescriptor()
        val argument = new InputArgument("foo", mode = InputArgument.OPTIONAL, default = Some("bar"))
        val definition = new InputDefinition(arguments = List(argument))

        val result = descriptor.describe(application, definition, argument)

        assert(result.contains("bar"))
      }
    }
  }
}
