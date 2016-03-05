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

package eidolon.console.input.definition.parameter

import org.scalatest.FunSpec
import org.scalatest.Matchers._

/**
 * InputArgumentSpec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputArgumentSpec extends FunSpec {
  describe("eidolon.console.input.definition.parameter.InputArgument") {
    describe("validating it's name") {
      it("should not accept names that aren't at least 1 character long") {
        intercept[IllegalArgumentException] {
          val argument = new InputArgument("")
        }
      }

      it("should accept names that are 1 or more characters long") {
        noException should be thrownBy {
          val argument = new InputArgument("arg")
        }
      }
    }

    describe("validating it's mode") {
      it("should not accept modes that are less than 1") {
        intercept[IllegalArgumentException] {
          val argument = new InputArgument("opt", mode = 0)
        }
      }

      it("should not accept modes that are more than 3") {
        intercept[IllegalArgumentException] {
          val argument = new InputArgument("opt", mode = 4)
        }
      }

      it("should accept modes that from 1 to 3") {
        noException should be thrownBy {
          val argument1 = new InputArgument("opt", mode = 1)
          val argument2 = new InputArgument("opt", mode = 2)
          val argument3 = new InputArgument("opt", mode = 3)
        }
      }
    }

    describe("validating it's default value") {
      it("should not accept a default vcalue is a value is required") {
        intercept[IllegalArgumentException] {
          val argument = new InputArgument(
            name = "arg",
            mode = InputArgument.REQUIRED,
            default = Some("default")
          )
        }
      }

      it("should accept a default value if a value is optional") {
        noException should be thrownBy {
          val argument = new InputArgument(
            name = "arg",
            mode = InputArgument.OPTIONAL,
            default = Some("default")
          )
        }
      }

      it("should accept no default value if a value is required") {
        noException should be thrownBy {
          val argument = new InputArgument(
            name = "arg",
            mode = InputArgument.REQUIRED,
            default = None
          )
        }
      }

      it("should accept no default value if a value is optional") {
        noException should be thrownBy {
          val argument = new InputArgument(
            name = "arg",
            mode = InputArgument.OPTIONAL,
            default = None
          )
        }
      }
    }

    describe("hasDefault()") {
      it("should return true if a default value is set") {
        val argument = new InputArgument("arg", default = Some("default"))

        assert(argument.hasDefault)
      }

      it("should return false if a default value is not set") {
        val argument = new InputArgument(name = "arg", default = None)

        assert(!argument.hasDefault)
      }
    }

    describe("isOptional()") {
      it("should return true if the argument is optional") {
        val argument = new InputArgument("arg", mode = InputArgument.OPTIONAL)

        assert(argument.isOptional)
      }

      it("should return false if the argument is required") {
        val argument = new InputArgument("arg", mode = InputArgument.REQUIRED)

        assert(!argument.isOptional)
      }
    }

    describe("isrequired()") {
      it("should return false if the argument is optional") {
        val argument = new InputArgument("arg", mode = InputArgument.OPTIONAL)

        assert(!argument.isRequired)
      }

      it("should return true if the argument is required") {
        val argument = new InputArgument("arg", mode = InputArgument.REQUIRED)

        assert(argument.isRequired)
      }
    }
  }
}
