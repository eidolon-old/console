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

import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.definition.parameter.{InputOption, InputArgument}
import eidolon.console.input.parser.parameter.{ParsedInputShortOption, ParsedInputLongOption, ParsedInputArgument}
import eidolon.console.input.validation.parameter.{InvalidOption, ValidOption, ValidArgument, InvalidArgument}
import org.scalatest.FunSpec

/**
 * InputValidator Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputValidatorSpec extends FunSpec {
  describe("eidolon.console.input.validation.InputValidator") {
    describe("validate()") {
      describe("when validating arguments") {
        it("should set arguments that exist in the result as valid arguments") {
          val validator = new InputValidator()

          val definition = new InputDefinition(arguments = Map(
            "arg1" -> new InputArgument("arg1"),
            "arg2" -> new InputArgument("arg2")
          ))

          val result = validator.validate(definition, List(
            new ParsedInputArgument("foo"),
            new ParsedInputArgument("bar"),
            new ParsedInputArgument("baz")
          ))

          assert(result.validArguments.exists({
            case ValidArgument(name, argVal) => name == "arg1" && argVal == "foo"
          }))

          assert(result.validArguments.exists({
            case ValidArgument(name, argVal) => name == "arg2" && argVal == "bar"
          }))
        }

        it("should not set arguments that don't exist in the result") {
          val validator = new InputValidator()

          val definition = new InputDefinition(arguments = Map(
            "arg1" -> new InputArgument("arg1"),
            "arg2" -> new InputArgument("arg2")
          ))

          val result = validator.validate(definition, List())

          assert(result.invalidArguments.isEmpty)
          assert(result.validArguments.isEmpty)
        }

        it("should set missing required arguments in the result as invalid arguments") {
          val validator = new InputValidator()

          val definition = new InputDefinition(arguments = Map(
            "arg1" -> new InputArgument("arg1", mode = InputArgument.REQUIRED)
          ))

          val result = validator.validate(definition, List())

          assert(result.invalidArguments.size == 1)
          assert(result.invalidArguments.exists({
            case InvalidArgument(token) => token == "arg1"
          }))
        }

        it("should set missing arguments with default values in the result as valid arguments") {
          val validator = new InputValidator()

          val definition = new InputDefinition(arguments = Map(
            "arg1" -> new InputArgument("arg1", mode = InputArgument.OPTIONAL, default = Some("val1"))
          ))

          val result = validator.validate(definition, List())

          assert(result.validArguments.size == 1)
          assert(result.validArguments.exists({
            case ValidArgument(token, argVal) => token == "arg1" && argVal == "val1"
          }))
        }
      }

      describe("when validating long options") {
        describe("that expect no value") {
          it("should set them in the result as valid options") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption("opt1", mode = InputOption.VALUE_NONE)
            ))

            val result = validator.validate(definition, List(
              new ParsedInputLongOption("opt1", None)
            ))

            assert(result.validOptions.size == 1)
            assert(result.validOptions.exists({
              case ValidOption(name, optVal) => name == "opt1" && optVal.isEmpty
            }))
          }
        }

        describe("that have an optional value") {
          it("should set them as valid options in the result with the given value if one is provided") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                mode = InputOption.VALUE_OPTIONAL
              )
            ))

            val result = validator.validate(definition, List(
              new ParsedInputLongOption("opt1", Some("val1"))
            ))

            assert(result.validOptions.size == 1)
            assert(result.validOptions.exists({
              case ValidOption(name, optVal) => name == "opt1" && optVal.contains("val1")
            }))
          }

          it("should set them as valid options in the result with the default value if one is set and no value is given") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                mode = InputOption.VALUE_OPTIONAL,
                defaultValue = Some("val1")
              )
            ))

            val result = validator.validate(definition, List(
              new ParsedInputLongOption("opt1", None)
            ))

            assert(result.validOptions.size == 1)
            assert(result.validOptions.exists({
              case ValidOption(name, optVal) => name == "opt1" && optVal.contains("val1")
            }))
          }

          it("should set them as valid options in the result with no value if there is no value provided, and no default value") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                mode = InputOption.VALUE_OPTIONAL
              )
            ))

            val result = validator.validate(definition, List(
              new ParsedInputLongOption("opt1", None)
            ))

            assert(result.validOptions.size == 1)
            assert(result.validOptions.exists({
              case ValidOption(name, optVal) => name == "opt1" && optVal.isEmpty
            }))
          }
        }

        describe("that require a value") {
          it("should set them as valid options in the result with the given value if one is provided") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                mode = InputOption.VALUE_REQUIRED
              )
            ))

            val result = validator.validate(definition, List(
              new ParsedInputLongOption("opt1", Some("val1"))
            ))

            assert(result.validOptions.size == 1)
            assert(result.validOptions.exists({
              case ValidOption(name, optVal) => name == "opt1" && optVal.contains("val1")
            }))
          }

          it("should set them as invalid options in the result if no value is provided") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                mode = InputOption.VALUE_REQUIRED
              )
            ))

            val result = validator.validate(definition, List(
              new ParsedInputLongOption("opt1", None)
            ))

            assert(result.invalidOptions.size == 1)
            assert(result.invalidOptions.exists({
              case InvalidOption(token) => token == "opt1"
            }))
          }
        }

        describe("that are not defined but were given") {
          it("should set them as invalid options in the result") {
            val validator = new InputValidator()
            val definition = new InputDefinition(options = Map())

            val result = validator.validate(definition, List(
              new ParsedInputLongOption("opt1", None)
            ))

            assert(result.invalidOptions.size == 1)
            assert(result.invalidOptions.exists({
              case InvalidOption(token) => token == "opt1"
            }))
          }
        }

        describe("that have an unknown mode") {
          it("should throw throw an exception") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                mode = 3 // numbers not divisible by 2 aren't possible
              )
            ))

            intercept[RuntimeException] {
              validator.validate(definition, List(
                new ParsedInputLongOption("opt1", None)
              ))
            }
          }
        }
      }

      describe("when validating short options") {
        describe("that expect no value") {
          it("should set them in the result as valid options") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption("opt1", Some("o"), mode = InputOption.VALUE_NONE)
            ))

            val result = validator.validate(definition, List(
              new ParsedInputShortOption("o", None)
            ))

            assert(result.validOptions.size == 1)
            assert(result.validOptions.exists({
              case ValidOption(name, optVal) => name == "opt1" && optVal.isEmpty
            }))
          }
        }

        describe("that have an optional value") {
          it("should set them as valid options in the result with the given value if one is provided") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                shortName = Some("o"),
                mode = InputOption.VALUE_OPTIONAL
              )
            ))

            val result = validator.validate(definition, List(
              new ParsedInputShortOption("o", Some("val1"))
            ))

            assert(result.validOptions.size == 1)
            assert(result.validOptions.exists({
              case ValidOption(name, optVal) => name == "opt1" && optVal.contains("val1")
            }))
          }

          it("should set them as valid options in the result with the default value if one is set and no value is given") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                shortName = Some("o"),
                mode = InputOption.VALUE_OPTIONAL,
                defaultValue = Some("val1")
              )
            ))

            val result = validator.validate(definition, List(
              new ParsedInputShortOption("o", None)
            ))

            assert(result.validOptions.size == 1)
            assert(result.validOptions.exists({
              case ValidOption(name, optVal) => name == "opt1" && optVal.contains("val1")
            }))
          }

          it("should set them as valid options in the result with no value if there is no value provided, and no default value") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                shortName = Some("o"),
                mode = InputOption.VALUE_OPTIONAL
              )
            ))

            val result = validator.validate(definition, List(
              new ParsedInputShortOption("o", None)
            ))

            assert(result.validOptions.size == 1)
            assert(result.validOptions.exists({
              case ValidOption(name, optVal) => name == "opt1" && optVal.isEmpty
            }))
          }
        }

        describe("that require a value") {
          it("should set them as valid options in the result with the given value if one is provided") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                shortName = Some("o"),
                mode = InputOption.VALUE_REQUIRED
              )
            ))

            val result = validator.validate(definition, List(
              new ParsedInputShortOption("o", Some("val1"))
            ))

            assert(result.validOptions.size == 1)
            assert(result.validOptions.exists({
              case ValidOption(name, optVal) => name == "opt1" && optVal.contains("val1")
            }))
          }

          it("should set them as invalid options in the result if no value is provided") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                shortName = Some("o"),
                mode = InputOption.VALUE_REQUIRED
              )
            ))

            val result = validator.validate(definition, List(
              new ParsedInputShortOption("o", None)
            ))

            assert(result.invalidOptions.size == 1)
            assert(result.invalidOptions.exists({
              case InvalidOption(token) => token == "opt1"
            }))
          }
        }

        describe("that are not defined but were given") {
          it("should set them as invalid options in the result") {
            val validator = new InputValidator()
            val definition = new InputDefinition(options = Map())

            val result = validator.validate(definition, List(
              new ParsedInputShortOption("o", None)
            ))

            assert(result.invalidOptions.size == 1)
            assert(result.invalidOptions.exists({
              case InvalidOption(token) => token == "o"
            }))
          }
        }

        describe("that have an unknown mode") {
          it("should throw throw an exception") {
            val validator = new InputValidator()

            val definition = new InputDefinition(options = Map(
              "opt1" -> new InputOption(
                name = "opt1",
                shortName = Some("o"),
                mode = 3 // numbers not divisible by 2 aren't possible
              )
            ))

            intercept[RuntimeException] {
              validator.validate(definition, List(
                new ParsedInputShortOption("o", None)
              ))
            }
          }
        }
      }
    }
  }
}
