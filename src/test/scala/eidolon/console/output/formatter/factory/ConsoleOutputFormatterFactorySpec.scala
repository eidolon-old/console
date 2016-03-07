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

package eidolon.console.output.formatter.factory

import eidolon.chroma.Chroma
import eidolon.console.output.formatter.ConsoleOutputFormatter
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * ConsoleOutputFormatterFactorySpec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleOutputFormatterFactorySpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var chroma: Chroma = _

  before {
    chroma = mock[Chroma]
  }

  describe("eidolon.console.output.formatter.factory.ConsoleOutputFormatterFactory") {
    describe("build()") {
      it("should return a ConsoleOuputFormatter") {
        val factory = new ConsoleOutputFormatterFactory(chroma)
        val result = factory.build()

        assert(result.isInstanceOf[ConsoleOutputFormatter])
      }
    }
  }
}
