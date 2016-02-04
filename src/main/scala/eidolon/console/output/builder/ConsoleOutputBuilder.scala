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
import eidolon.console.output.formatter.style._
import eidolon.console.output.{ConsoleErrorOutput, ConsoleOutput, Output}

/**
 * Console Output Builder
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
final class ConsoleOutputBuilder extends OutputBuilder {
  private val chroma = Chroma()

  /**
   * @inheritdoc
   */
  override def build(): Output = {
    val formatter = new ConsoleOutputFormatter(chroma, buildStyleGroup())

    new ConsoleOutput(formatter, errOutput = new ConsoleErrorOutput(formatter))
  }

  private def buildStyleGroup(): OutputFormatterStyleGroup = {
    new OutputFormatterStyleGroup()
      .withStyle(new InfoOutputFormatterStyle(chroma))
      .withStyle(new ErrorOutputFormatterStyle(chroma))
      .withStyle(new CommentOutputFormatterStyle(chroma))
      .withStyle(new QuestionOutputFormatterStyle(chroma))
  }
}
