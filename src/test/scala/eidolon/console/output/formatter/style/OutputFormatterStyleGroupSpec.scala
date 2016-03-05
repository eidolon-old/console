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

import org.mockito.Mockito
import org.scalatest.FunSpec
import org.scalatest.mock.MockitoSugar

/**
 * OutputFormatterStyleGroup Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class OutputFormatterStyleGroupSpec extends FunSpec with MockitoSugar {
  describe("eidolon.console.output.formatter.style.OutputFormatterStyleGroup") {
    describe("withStyle()") {
      it("should create a new instance of the style group with the given style added") {
        val group = new OutputFormatterStyleGroup()
        val style1 = mock[OutputFormatterStyle]

        Mockito.when(style1.name).thenReturn("s1")

        val result = group.withStyle(style1)

        assert(group != result)
        assert(result.styles.exists({
          case (name, style) => name == "s1" && style == style1
        }))
      }
    }

    describe("withStyles()") {
      it("should create a new instance of the style group with the given styles added") {
        val group = new OutputFormatterStyleGroup()
        val style1 = mock[OutputFormatterStyle]
        val style2 = mock[OutputFormatterStyle]

        Mockito.when(style1.name).thenReturn("s1")
        Mockito.when(style2.name).thenReturn("s2")

        val result = group.withStyles(List(style1, style2))

        assert(group != result)

        assert(result.styles.exists({
          case (name, style) => name == "s1" && style == style1
        }))

        assert(result.styles.exists({
          case (name, style) => name == "s2" && style == style2
        }))
      }
    }
  }
}
