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

package eidolon.console.input.parser

import eidolon.console.input.parser.parameter.{ParsedInputArgument, ParsedInputShortOption, ParsedInputLongOption}
import org.scalatest.FunSpec

/**
 * InputParser Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputParserSpec extends FunSpec {
  describe("eidolon.console.input.parser.InputParser") {
    describe("parse()") {
      it("should parse strings starting with '--' as long options") {
        val parser = new InputParser()
        val result = parser.parse(List(
          "--foo",
          "--bar"
        ))

        assert(result.size == 2)

        assert(result.exists({
          case ParsedInputLongOption(token, _) => token == "foo"
        }))

        assert(result.exists({
          case ParsedInputLongOption(token, _) => token == "bar"
        }))
      }

      it("should stop parsing after it finds '--' alone") {
        val parser = new InputParser()
        val result = parser.parse(List(
          "--foo",
          "-b",
          "baz",
          "--",
          "--qux"
        ))

        assert(result.size == 3)
      }

      it("should be able to parse long options with values") {
        val parser = new InputParser()
        val result = parser.parse(List(
          "--foo=bar"
        ))

        assert(result.size == 1)
        assert(result.exists({
          case ParsedInputLongOption(token, value) => token == "foo" && value.contains("bar")
        }))
      }

      it("should be able to parse long options without values") {
        val parser = new InputParser()
        val result = parser.parse(List(
          "--foo"
        ))

        assert(result.size == 1)
        assert(result.exists({
          case ParsedInputLongOption(token, value) => token == "foo" && value.isEmpty
        }))
      }

      it("should parse strings starting with '-' as short options") {
        val parser = new InputParser()
        val result = parser.parse(List(
          "-f",
          "-b"
        ))

        assert(result.size == 2)

        assert(result.exists({
          case ParsedInputShortOption(token, _) => token == "f"
        }))

        assert(result.exists({
          case ParsedInputShortOption(token, _) => token == "b"
        }))
      }

      it("should not parse '-' as a short option, it should be parsed as an argument") {
        val parser = new InputParser()
        val result = parser.parse(List(
          "-"
        ))

        assert(result.size == 1)
        assert(result.exists({
          case ParsedInputArgument(token) => token == "-"
        }))
      }

      it("should be able to parse short options with values") {
        val parser = new InputParser()
        val result = parser.parse(List(
          "-f=bar"
        ))

        assert(result.size == 1)
        assert(result.exists({
          case ParsedInputShortOption(token, value) => token == "f" && value.contains("bar")
        }))
      }

      it("should be able to parse short options without values") {
        val parser = new InputParser()
        val result = parser.parse(List(
          "-f"
        ))

        assert(result.size == 1)
        assert(result.exists({
          case ParsedInputShortOption(token, value) => token == "f" && value.isEmpty
        }))
      }

      it("should parse strings that don't start with either '--' or '-' as arguments") {
        val parser = new InputParser()
        val result = parser.parse(List(
          "foo",
          "bar"
        ))

        assert(result.size == 2)

        assert(result.exists({
          case ParsedInputArgument(token) => token == "foo"
        }))

        assert(result.exists({
          case ParsedInputArgument(token) => token == "bar"
        }))
      }
    }
  }
}
