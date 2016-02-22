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

import eidolon.console.output.Output
import org.scalatest.FunSpec

/**
 * OutputFactory Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class OutputFactorySpec extends FunSpec {
  describe("eidolon.console.output.factory.OutputFactory") {
    describe("build()") {
      it("should return an instance of Output") {
        val factory = new OutputFactory()
        val output = factory.build()

        assert(output.isInstanceOf[Output])
      }
    }
  }
}
