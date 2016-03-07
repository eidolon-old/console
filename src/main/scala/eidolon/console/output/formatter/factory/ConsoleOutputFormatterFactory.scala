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

package eidolon.console.output.formatter.factory

import eidolon.chroma.Chroma
import eidolon.console.output.formatter.{ConsoleOutputFormatter, OutputFormatter}
import eidolon.console.output.formatter.style._

/**
 * ConsoleOutputFormatter Factory
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleOutputFormatterFactory(
    private val chroma: Chroma)
  extends OutputFormatterFactory {

  /**
   * @inheritdoc
   */
  override def build(): OutputFormatter = {
    new ConsoleOutputFormatter(buildStyleGroup())
  }

  /**
   * Build the default style group
   *
   * @return a style group
   */
  private def buildStyleGroup(): OutputFormatterStyleGroup = {
    new OutputFormatterStyleGroup()
      .withStyle(new InfoOutputFormatterStyle(chroma))
      .withStyle(new ErrorOutputFormatterStyle(chroma))
      .withStyle(new CommentOutputFormatterStyle(chroma))
      .withStyle(new QuestionOutputFormatterStyle(chroma))
  }
}
