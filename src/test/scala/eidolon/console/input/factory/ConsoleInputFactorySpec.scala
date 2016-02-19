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

package eidolon.console.input.factory

import eidolon.console.input.ConsoleInput
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * ConsoleInputFactory Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleInputFactorySpec extends FunSpec with BeforeAndAfter {
  describe("eidolon.console.input.factory.ConsoleInputFactory") {
    describe("build()") {
      it("should return a ConsoleInput") {
        val factory = new ConsoleInputFactory()
        val input = factory.build()

        assert(input.isInstanceOf[ConsoleInput])
      }
    }
  }
}
