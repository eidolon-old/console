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
 * InputOption Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputOptionSpec extends FunSpec {
  describe("eidolon.console.input.definition.parameter.InputOption") {
    describe("validating it's name") {
      it("should not accept names that aren't at least 1 character long") {
        intercept[IllegalArgumentException] {
          val option = new InputOption("")
        }
      }

      it("should accept names that are 1 or more characters long") {
        noException should be thrownBy {
          val option = new InputOption("opt")
        }
      }
    }

    describe("validating it's short name") {
      it("should not accept short names that are less than 1 character long") {
        intercept[IllegalArgumentException] {
          val option = new InputOption("opt", Some(""))
        }
      }

      it("should not accept short names that are more than 1 character long") {
        intercept[IllegalArgumentException] {
          val option = new InputOption("opt", Some("opt"))
        }
      }

      it("should accept short names that are 1 character long") {
        noException should be thrownBy {
          val option = new InputOption("opt", Some("o"))
        }
      }

      it("should accept no short name") {
        noException should be thrownBy {
          val option = new InputOption("opt", None)
        }
      }
    }

    describe("validating it's mode") {
      it("should not accept modes that are less than 1") {
        intercept[IllegalArgumentException] {
          val option = new InputOption("opt", mode = 0)
        }
      }

      it("should not accept modes that are more than 7") {
        intercept[IllegalArgumentException] {
          val option = new InputOption("opt", mode = 8)
        }
      }

      it("should accept modes that from 1 to 7") {
        noException should be thrownBy {
          val option1 = new InputOption("opt", mode = 1)
          val option2 = new InputOption("opt", mode = 2)
          val option3 = new InputOption("opt", mode = 3)
          val option4 = new InputOption("opt", mode = 4)
          val option5 = new InputOption("opt", mode = 5)
          val option6 = new InputOption("opt", mode = 6)
          val option7 = new InputOption("opt", mode = 7)
        }
      }
    }

    describe("validating it's default value") {
      it("should not accept a default value if a value is required") {
        intercept[IllegalArgumentException] {
          val option = new InputOption(
            name = "opt",
            mode = InputOption.VALUE_REQUIRED,
            defaultValue = Some("default")
          )
        }
      }

      it("should not accept a default value if no value is required") {
        intercept[IllegalArgumentException] {
          val option = new InputOption(
            name = "opt",
            mode = InputOption.VALUE_NONE,
            defaultValue = Some("default")
          )
        }
      }

      it("should accept a default value if a value is optional") {
        noException should be thrownBy {
          val option = new InputOption(
            name = "opt",
            mode = InputOption.VALUE_OPTIONAL,
            defaultValue = Some("default")
          )
        }
      }

      it("should accept no default value if a value is required") {
        noException should be thrownBy {
          val option = new InputOption(
            name = "opt",
            mode = InputOption.VALUE_REQUIRED,
            defaultValue = None
          )
        }
      }

      it("should accept no default value if no value is required") {
        noException should be thrownBy {
          val option = new InputOption(
            name = "opt",
            mode = InputOption.VALUE_NONE,
            defaultValue = None
          )
        }
      }

      it("should accept no default value if a value is optional") {
        noException should be thrownBy {
          val option = new InputOption(
            name = "opt",
            mode = InputOption.VALUE_OPTIONAL,
            defaultValue = None
          )
        }
      }
    }

    describe("hasDefaultValue()") {
      it("should return true if a default value is set") {
        val option = new InputOption(
          name = "opt",
          mode = InputOption.VALUE_OPTIONAL,
          defaultValue = Some("default")
        )

        assert(option.hasDefaultValue)
      }

      it("should return false if a default value is not set") {
        val option = new InputOption(
          name = "opt",
          mode = InputOption.VALUE_OPTIONAL,
          defaultValue = None
        )

        assert(!option.hasDefaultValue)
      }
    }

    describe("acceptValue()") {
      it("should return false if the option doesn't expect a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_NONE)

        assert(!option.acceptsValue)
      }

      it("should return true if the option optionally takes a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_OPTIONAL)

        assert(option.acceptsValue)
      }

      it("should return true if the option requires a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_REQUIRED)

        assert(option.acceptsValue)
      }
    }

    describe("isNoValue()") {
      it("should return true if the option doesn't expect a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_NONE)

        assert(option.isNoValue)
      }

      it("should return false if the option optionally takes a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_OPTIONAL)

        assert(!option.isNoValue)
      }

      it("should return false if the option requires a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_REQUIRED)

        assert(!option.isNoValue)
      }
    }

    describe("isOptionalValue()") {
      it("should return false if the option doesn't expect a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_NONE)

        assert(!option.isOptionalValue)
      }

      it("should return true if the option optionally takes a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_OPTIONAL)

        assert(option.isOptionalValue)
      }

      it("should return false if the option requires a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_REQUIRED)

        assert(!option.isOptionalValue)
      }
    }

    describe("isRequiredValue()") {
      it("should return false if the option doesn't expect a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_NONE)

        assert(!option.isRequiredValue)
      }

      it("should return false if the option optionally takes a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_OPTIONAL)

        assert(!option.isRequiredValue)
      }

      it("should return true if the option requires a value") {
        val option = new InputOption("opt", mode = InputOption.VALUE_REQUIRED)

        assert(option.isRequiredValue)
      }
    }
  }
}
