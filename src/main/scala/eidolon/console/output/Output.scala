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

package eidolon.console.output

import eidolon.console.output.formatter.OutputFormatter

/**
 * Output
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
abstract class Output(
    val formatter: OutputFormatter[_],
    val verbosity: Int = Output.VerbosityNormal) {

  def write(
      message: String,
      newLine: Boolean = true,
      mode: Int = Output.OutputNormal,
      verbosity: Int = Output.VerbosityNormal)
    : Unit = {

    if (verbosity <= this.verbosity) {
      val formatted = formatter.format(message, mode)

      doWrite(formatted, newLine)
    }
  }

  protected def doWrite(message: String, newLine: Boolean): Unit
}

object Output {
  val VerbosityQuiet = 16;
  val VerbosityNormal = 32;
  val VerbosityVerbose = 64;
  val VerbosityVeryVerbose = 128;
  val VerbosityDebug = 256;

  val OutputNormal = 1
  val OutputRaw = 2
  val OutputPlain = 4
}
