/**
 * This file is part of the "console" project.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the LICENSE is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

import eidolon.console.output.ConsoleOutput

/**
 * FormatterMain
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object FormatterMain extends App {
  val output = new ConsoleOutput()

  output.write("<info>Hey there</info>, how are <error>you</error>?")
}
