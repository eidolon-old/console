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

import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * InputValidator Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputValidatorSpec extends FunSpec with BeforeAndAfter {
  describe("eidolon.console.input.validation.InputValidator") {
    describe("validate()") {
      describe("when validating arguments") {
        it("should set arguments that exist in the result as valid arguments") {
          pending
        }

        it("should set arguments that don't exist in the result as invalid arguments") {
          pending
        }

        it("should set missing required arguments in the result as invalid arguments") {
          pending
        }

        it("should set missing arguments with default values in the result as valid arguments") {
          pending
        }
      }

      describe("when validating long options") {
        describe("that expect no value") {
          it("should set them in the result as valid options") {
            // case InputOption.VALUE_NONE
            pending
          }
        }

        describe("that have an optional value") {
          it("should set them as valid options in the result with the given value if one is provided") {
            // case InputOption.VALUE_OPTIONAL, value not empty
            pending
          }

          it("should set them as valid options in the result with the default value if one is set and no value is given") {
            // case InputOption.VALUE_OPTIONAL, value empty, default not empty
            pending
          }

          it("should set them as valid options in the result with no value if there is no value provided, and no default value") {
            // case InputOption.VALUE_OPTIONAL, value empty, default empty
            pending
          }
        }

        describe("that require a value") {
          it("should set them as valid options in the result with the given value if one is provided") {
            pending
          }

          it("should set them as invalid options in the result if no value is provided") {
            pending
          }
        }

        describe("that have an unknown mode") {
          it("should throw throw an exception") {
            pending
          }
        }
      }

      describe("when validating short options") {
        describe("that expect no value") {
          it("should set them in the result as valid options") {
            // case InputOption.VALUE_NONE
            pending
          }
        }

        describe("that have an optional value") {
          it("should set them as valid options in the result with the given value if one is provided") {
            // case InputOption.VALUE_OPTIONAL, value not empty
            pending
          }

          it("should set them as valid options in the result with the default value if one is set and no value is given") {
            // case InputOption.VALUE_OPTIONAL, value empty, default not empty
            pending
          }

          it("should set them as valid options in the result with no value if there is no value provided, and no default value") {
            // case InputOption.VALUE_OPTIONAL, value empty, default empty
            pending
          }
        }

        describe("that require a value") {
          it("should set them as valid options in the result with the given value if one is provided") {
            pending
          }

          it("should set them as invalid options in the result if no value is provided") {
            pending
          }
        }

        describe("that have an unknown mode") {
          it("should throw throw an exception") {
            pending
          }
        }
      }
    }
  }
}
