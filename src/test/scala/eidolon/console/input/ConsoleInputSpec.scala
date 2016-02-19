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

import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * ConsoleInput Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleInputSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  describe("eidolon.console.input.ConsoleInputSpec") {
    describe("withArgument()") {
      it("should return a ConsoleInput") {
        val input = new ConsoleInput()
        val result = input.withArgument("testName", "testValue")

        assert(result.isInstanceOf[ConsoleInput])
      }

      it("should return a new instance of ConsoleInput") {
        val input = new ConsoleInput()
        val result = input.withArgument("testName", "testValue")

        assert(!input.equals(result))
      }

      it("should return a ConsoleInput with the passed argument") {
        val input = new ConsoleInput()
        val result = input.withArgument("testName", "testValue")

        assert(!input.arguments.exists(_ == ("testName" -> "testValue")))
        assert(result.arguments.exists(_ == ("testName" -> "testValue")))
      }
    }

    describe("withArguments()") {
      it("should return a ConsoleInput") {
        val input = new ConsoleInput()
        val result = input.withArguments(Map())

        assert(result.isInstanceOf[ConsoleInput])
      }

      it("should return a new instance of ConsoleInput") {
        val input = new ConsoleInput()
        val result = input.withArguments(Map())

        assert(!input.equals(result))
      }

      it("should return a ConsoleInput with the passed arguments") {
        val input = new ConsoleInput()
        val result = input.withArguments(Map("key" -> "value", "key2" -> "value2"))

        assert(!input.arguments.exists(_ == ("key" -> "value")))
        assert(!input.arguments.exists(_ == ("key2" -> "value2")))
        assert(result.arguments.exists(_ == ("key" -> "value")))
        assert(result.arguments.exists(_ == ("key2" -> "value2")))
      }
    }

    describe("withOption()") {
      it("should return a ConsoleInput") {
        val input = new ConsoleInput()
        val result = input.withOption("testName", Some("testValue"))

        assert(result.isInstanceOf[ConsoleInput])
      }

      it("should return a new instance of ConsoleInput") {
        val input = new ConsoleInput()
        val result = input.withOption("testName", Some("testValue"))

        assert(!input.equals(result))
      }

      it("should return a ConsoleInput with the passed option") {
        val input = new ConsoleInput()
        val result = input.withOption("testName", Some("testValue"))

        assert(!input.options.exists(_ == ("testName" -> Some("testValue"))))
        assert(result.options.exists(_ == "testName" -> Some("testValue")))
      }
    }

    describe("withOptions()") {
      it("should return a ConsoleInput") {
        val input = new ConsoleInput()
        val result = input.withOptions(Map())

        assert(result.isInstanceOf[ConsoleInput])
      }

      it("should return a new instance of ConsoleInput") {
        val input = new ConsoleInput()
        val result = input.withOptions(Map())

        assert(!input.equals(result))
      }

      it("should return a ConsoleInput with the passed options") {
        val input = new ConsoleInput()
        val result = input.withOptions(Map("key" -> Some("value"), "key2" -> Some("value2")))

        assert(!input.options.exists(_ == ("key" -> Some("value"))))
        assert(!input.options.exists(_ == ("key2" -> Some("value2"))))
        assert(result.options.exists(_ == ("key" -> Some("value"))))
        assert(result.options.exists(_ == ("key2" -> Some("value2"))))
      }
    }
  }
}
