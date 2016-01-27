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

package eidolon.console.output.builder

import eidolon.chroma.Chroma
import eidolon.console.output.formatter.ConsoleOutputFormatter
import eidolon.console.output.formatter.lexer.OutputFormatLexer
import eidolon.console.output.formatter.parser.OutputFormatParser
import eidolon.console.output.{ConsoleErrorOutput, ConsoleOutput, Output}

/**
 * ConsoleOutputBuilder
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleOutputBuilder extends OutputBuilder {
  /**
   * @inheritdoc
   */
  override def build(): Output = {
    val lexer = new OutputFormatLexer()
    val parser = new OutputFormatParser()
    val formatter = new ConsoleOutputFormatter(Chroma(), lexer, parser)

    new ConsoleOutput(formatter, errOutput = new ConsoleErrorOutput(formatter))
  }
}
