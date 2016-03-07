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

package eidolon.console.dialog.factory

import eidolon.console.dialog.Dialog
import eidolon.console.output.formatter.OutputFormatter
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * DialogFactory Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class DialogFactorySpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var formatter: OutputFormatter = _

  before {
    formatter = mock[OutputFormatter]
  }

  describe("eidolon.console.dialog.factory.DialogFactory") {
    describe("build()") {
      it("should return an instance of Dialog") {
        val factory = new DialogFactory(formatter)
        val result = factory.build()

        assert(result.isInstanceOf[Dialog])
      }
    }
  }
}
