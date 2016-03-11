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

package eidolon.console.input

import org.scalatest.FunSpec

/**
 * Input Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputSpec extends FunSpec {
  describe("eidolon.console.input.InputSpec") {
    describe("withArgument()") {
      it("should return a Input") {
        val input = new Input()
        val result = input.withArgument("testName", "testValue")

        assert(result.isInstanceOf[Input])
      }

      it("should return a new instance of Input") {
        val input = new Input()
        val result = input.withArgument("testName", "testValue")

        assert(!input.equals(result))
      }

      it("should return a Input with the passed argument") {
        val input = new Input()
        val result = input.withArgument("testName", "testValue")

        assert(!input.arguments.exists(_ == ("testName" -> "testValue")))
        assert(result.arguments.exists(_ == ("testName" -> "testValue")))
      }
    }

    describe("withArguments()") {
      it("should return a Input") {
        val input = new Input()
        val result = input.withArguments(Map())

        assert(result.isInstanceOf[Input])
      }

      it("should return a new instance of Input") {
        val input = new Input()
        val result = input.withArguments(Map())

        assert(!input.equals(result))
      }

      it("should return a Input with the passed arguments") {
        val input = new Input()
        val result = input.withArguments(Map("key" -> "value", "key2" -> "value2"))

        assert(!input.arguments.exists(_ == ("key" -> "value")))
        assert(!input.arguments.exists(_ == ("key2" -> "value2")))
        assert(result.arguments.exists(_ == ("key" -> "value")))
        assert(result.arguments.exists(_ == ("key2" -> "value2")))
      }
    }

    describe("withOption()") {
      it("should return a Input") {
        val input = new Input()
        val result = input.withOption("testName", Some("testValue"))

        assert(result.isInstanceOf[Input])
      }

      it("should return a new instance of Input") {
        val input = new Input()
        val result = input.withOption("testName", Some("testValue"))

        assert(!input.equals(result))
      }

      it("should return a Input with the passed option") {
        val input = new Input()
        val result = input.withOption("testName", Some("testValue"))

        assert(!input.options.exists(_ == ("testName" -> Some("testValue"))))
        assert(result.options.exists(_ == "testName" -> Some("testValue")))
      }
    }

    describe("withOptions()") {
      it("should return a Input") {
        val input = new Input()
        val result = input.withOptions(Map())

        assert(result.isInstanceOf[Input])
      }

      it("should return a new instance of Input") {
        val input = new Input()
        val result = input.withOptions(Map())

        assert(!input.equals(result))
      }

      it("should return a Input with the passed options") {
        val input = new Input()
        val result = input.withOptions(Map("key" -> Some("value"), "key2" -> Some("value2")))

        assert(!input.options.exists(_ == ("key" -> Some("value"))))
        assert(!input.options.exists(_ == ("key2" -> Some("value2"))))
        assert(result.options.exists(_ == ("key" -> Some("value"))))
        assert(result.options.exists(_ == ("key2" -> Some("value2"))))
      }
    }
  }
}
