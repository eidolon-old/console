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
final class ArgsInputParser(
    private val args: Array[String],
    private val definition: InputDefinition)
  extends InputParser {

  override def parse(): Set[InputParameter] = {
    args
      .takeWhile(_ != "--")
      .map({
        case token if token == "" =>
          parseArgument(token)
        case token if token.startsWith("--") =>
          parseLongOption(token)
        case token if token.startsWith("-") && token != "-" =>
          parseShortOption(token)
        case token =>
          parseArgument(token)
      })
      .toSet
  }

  private def parseArgument(token: String): InputArgument = {
    new InputArgument(token, InputArgument.IS_ARRAY, default = Some(Seq("Testing")))
  }

  private def parseShortOption(token: String): InputOption = {
    new InputOption(token, None, InputOption.VALUE_REQUIRED)
  }

  private def parseLongOption(token: String): InputOption = {
    val name = token.drop(2)

    if (!definition.hasOption(name)) {
      throw new RuntimeException("The option '--%s' does not exist.".format(name))
    }

    definition.getOption(name).get
  }
}
