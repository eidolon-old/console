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

package eidolon.console.output.factory

import java.io.PrintStream

import eidolon.console.output.Output
import eidolon.console.output.formatter.OutputFormatter
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * OutputFactory Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class OutputFactorySpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var outputFormatter: OutputFormatter = _
  var outputStream: PrintStream = _

  before {
    outputFormatter = mock[OutputFormatter]
    outputStream = mock[PrintStream]
  }

  describe("eidolon.console.output.factory.OutputFactory") {
    describe("build()") {
      it("should return an instance of Output") {
        val factory = new OutputFactory(outputStream, outputStream, outputFormatter)
        val output = factory.build(Output.VerbosityNormal)

        assert(output.isInstanceOf[Output])
      }
    }
  }
}
