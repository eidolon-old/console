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

package eidolon.console.output.formatter.style

import eidolon.chroma.Chroma
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.mock.MockitoSugar
import org.mockito.{Mockito, Matchers}

import scala.collection.mutable

/**
 * CommentOutputFormatterStyle Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class CommentOutputFormatterStyleSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var chroma: Chroma = _

  before {
    chroma = mock[Chroma]

    Mockito
      .when(chroma.applyDynamic(Matchers.anyString())(Matchers.anyString()))
      .thenAnswer(new Answer[String] {
        override def answer(invocation: InvocationOnMock): String = {
          val input = invocation
            .getArgumentAt(1, classOf[mutable.WrappedArray[String]])
            .headOption
            .get

          input + "-styled"
        }
      })
  }

  describe("eidolon.console.output.formatter.style.CommentOutputFormatterStyle") {
    describe("applyStyle()") {
      it("should apply a style") {
        val style = new CommentOutputFormatterStyle(chroma)
        val result = style.applyStyle("foo")

        assert(result == "foo-styled")
      }
    }
  }
}
