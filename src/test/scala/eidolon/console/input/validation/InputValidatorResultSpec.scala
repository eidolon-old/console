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

package eidolon.console.input.validation

import eidolon.console.input.validation.parameter.{InvalidArgument, InvalidOption, ValidArgument, ValidOption}
import org.scalatest.FunSpec

/**
 * InputValidatorResult Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputValidatorResultSpec extends FunSpec {
  describe("eidolon.console.input.validation.InputValidatorResult") {
    describe("invalidArguments") {
      it("should contain all of the invalid arguments") {
        val validArguments = List(new ValidArgument("varg1", "val1"), new ValidArgument("varg2", "val2"))
        val invalidArguments = List(new InvalidArgument("ivarg1"), new InvalidArgument("ivarg2"))
        val validOptions = List(new ValidOption("vopt1"), new ValidOption("vopt2"))
        val invalidOptions = List(new InvalidOption("ivopt1"), new InvalidOption("ivopt2"))

        val result = new InputValidatorResult(
          invalid = invalidArguments ++: invalidOptions,
          valid = validArguments ++: validOptions
        )

        assert(result.invalidArguments.equals(invalidArguments))
      }
    }

    describe("invalidOptions") {
      it("should contain all of the invalid options") {
        val validArguments = List(new ValidArgument("varg1", "val1"), new ValidArgument("varg2", "val2"))
        val invalidArguments = List(new InvalidArgument("ivarg1"), new InvalidArgument("ivarg2"))
        val validOptions = List(new ValidOption("vopt1"), new ValidOption("vopt2"))
        val invalidOptions = List(new InvalidOption("ivopt1"), new InvalidOption("ivopt2"))

        val result = new InputValidatorResult(
          invalid = invalidArguments ++: invalidOptions,
          valid = validArguments ++: validOptions
        )

        assert(result.invalidOptions.equals(invalidOptions))
      }
    }

    describe("validArguments") {
      it("should contain all of the valid arguments") {
        val validArguments = List(new ValidArgument("varg1", "val1"), new ValidArgument("varg2", "val2"))
        val invalidArguments = List(new InvalidArgument("ivarg1"), new InvalidArgument("ivarg2"))
        val validOptions = List(new ValidOption("vopt1"), new ValidOption("vopt2"))
        val invalidOptions = List(new InvalidOption("ivopt1"), new InvalidOption("ivopt2"))

        val result = new InputValidatorResult(
          invalid = invalidArguments ++: invalidOptions,
          valid = validArguments ++: validOptions
        )

        assert(result.validArguments.equals(validArguments))
      }
    }

    describe("validOptions") {
      it("should contain all of the valid options") {
        val validArguments = List(new ValidArgument("varg1", "val1"), new ValidArgument("varg2", "val2"))
        val invalidArguments = List(new InvalidArgument("ivarg1"), new InvalidArgument("ivarg2"))
        val validOptions = List(new ValidOption("vopt1"), new ValidOption("vopt2"))
        val invalidOptions = List(new InvalidOption("ivopt1"), new InvalidOption("ivopt2"))

        val result = new InputValidatorResult(
          invalid = invalidArguments ++: invalidOptions,
          valid = validArguments ++: validOptions
        )

        assert(result.validOptions.equals(validOptions))
      }
    }

    describe("argumentCount") {
      it("should equal the number of arguments passed to it, not counting options") {
        val result = new InputValidatorResult(
          invalid = List(new InvalidArgument("arg1")),
          valid = List(new ValidArgument("arg2", "val1"), new ValidOption("opt1"))
        )

        assert(result.argumentCount == 2)
      }
    }

    describe("optionCount") {
      it("should equal the number of options passed to it, not counting arguments") {
        val result = new InputValidatorResult(
          invalid = List(new InvalidOption("opt1")),
          valid = List(new ValidArgument("arg1", "val1"), new ValidOption("opt2"))
        )

        assert(result.optionCount == 2)
      }
    }

    describe("argumentNames") {
      it("should be a list of all of the argument names, not including options") {
        val result = new InputValidatorResult(
          invalid = List(new InvalidArgument("arg1")),
          valid = List(new ValidArgument("arg2", "val1"), new ValidOption("opt1"))
        )

        assert(result.argumentNames.contains("arg1"))
        assert(result.argumentNames.contains("arg2"))
        assert(!result.argumentNames.contains("opt1"))
      }
    }

    describe("optionNames") {
      it("should be a list of all of the option names, not including arguments") {
        val result = new InputValidatorResult(
          invalid = List(new InvalidOption("opt1")),
          valid = List(new ValidArgument("arg1", "val1"), new ValidOption("opt2"))
        )

        assert(result.optionNames.contains("opt1"))
        assert(result.optionNames.contains("opt2"))
        assert(!result.optionNames.contains("arg1"))
      }
    }

    describe("isValid()") {
      it("should return true if there are no invalid parameters") {
        val result = new InputValidatorResult()

        assert(result.isValid)
      }

      it("should return false if there are invalid parameters") {
        val result = new InputValidatorResult(invalid = List(new InvalidOption("test")))

        assert(!result.isValid)
      }
    }
  }
}
