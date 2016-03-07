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

package eidolon.console

import jline.console.ConsoleReader
import org.scalatest.FunSpec
import org.scalatest.mock.MockitoSugar

/**
 * Foo Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class FooSpec extends FunSpec with MockitoSugar {
  val password = new ConsoleReader().readLine(new Character('*'))

  println(password)
}
