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
import eidolon.console.input.definition.parameter.InputOption
import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.mock.MockitoSugar

/**
 * InputOptionDescriptor Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputOptionDescriptorSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var application: Application = _

  before {
    application = mock[Application]
  }

  describe("eidolon.console.descriptor.InputOptionDescriptor") {
    describe("describe()") {
      it("should include the option's name") {
        val descriptor = new InputOptionDescriptor()
        val option = new InputOption("foo")
        val definition = new InputDefinition(options = List(option))

        val result = descriptor.describe(application, definition, option)

        assert(result.contains("--foo"))
      }

      it("should include the option's short name if one is set") {
        val descriptor = new InputOptionDescriptor()
        val option = new InputOption("foo", shortName = Some("b"))
        val definition = new InputDefinition(options = List(option))

        val result = descriptor.describe(application, definition, option)

        assert(result.contains("-b"))
      }

      it("should indicate that the option accepts a value if it does") {
        val descriptor = new InputOptionDescriptor()
        val option = new InputOption("foo", mode = InputOption.VALUE_OPTIONAL)
        val definition = new InputDefinition(options = List(option))

        val result = descriptor.describe(application, definition, option)

        assert(result.contains("=FOO"))
      }

      it("should indicate that the option requires a value if it does") {
        val descriptor = new InputOptionDescriptor()
        val option = new InputOption("foo", mode = InputOption.VALUE_REQUIRED)
        val definition = new InputDefinition(options = List(option))

        val result = descriptor.describe(application, definition, option)

        assert(result.contains("=FOO"))
      }

      it("should indicate that the option optionally accepts a value if it does") {
        val descriptor = new InputOptionDescriptor()
        val option = new InputOption("foo", mode = InputOption.VALUE_OPTIONAL)
        val definition = new InputDefinition(options = List(option))

        val result = descriptor.describe(application, definition, option)

        assert(result.contains("[=FOO]"))
      }

      it("should include the option's description if one is set") {
        val descriptor = new InputOptionDescriptor()
        val option = new InputOption("foo", description = Some("foo bar baz qux"))
        val definition = new InputDefinition(options = List(option))

        val result = descriptor.describe(application, definition, option)

        assert(result.contains("foo bar baz qux"))
      }

      it("should describe the option's default if one is set") {
        val descriptor = new InputOptionDescriptor()
        val option = new InputOption("foo", mode = InputOption.VALUE_OPTIONAL, defaultValue = Some("bar"))
        val definition = new InputDefinition(options = List(option))

        val result = descriptor.describe(application, definition, option)

        assert(result.contains("bar"))
      }
    }
  }
}
