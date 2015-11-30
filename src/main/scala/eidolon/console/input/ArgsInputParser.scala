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

/**
 * Args Input Parser
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ArgsInputParser(
    args: Array[String])
  extends InputParser {

  override def parse(): Array[InputParameter] = {
    args
      .takeWhile({
        case token if token == "--" => false
        case _ => true
      })
      .map({
        case token if token == "" =>
          parseArgument(token + ": argument (empty)")
        case token if token.startsWith("--") =>
          parseLongOption(token + ": long option")
        case token if token.startsWith("-") && token != "-" =>
          parseShortOption(token + ": short option")
        case token =>
          parseArgument(token + ": argument")
      })
  }

  private def parseArgument(token: String): InputArgument = {
    new InputArgument(token, InputArgument.IS_ARRAY, default = Some(Array("Testing")))
  }

  private def parseShortOption(token: String): InputOption = {
    new InputOption(Some(token), None, InputOption.VALUE_REQUIRED)
  }

  private def parseLongOption(token: String): InputOption = {
    new InputOption(Some(token), None, InputOption.VALUE_NONE)
  }
}
