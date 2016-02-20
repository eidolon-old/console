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

package eidolon.console.output

import eidolon.console.output.formatter.OutputFormatter

/**
 * Output
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait Output {
  val formatter: OutputFormatter
  val verbosity: Int = Output.VerbosityNormal

  /**
   * Check if this output's verbosity level is quiet
   *
   * @return true if verbosity level is quiet
   */
  def isQuiet: Boolean = {
    Output.VerbosityQuiet == verbosity
  }

  /**
   * Check if this output's verbosity level is verbose
   *
   * @return true if verbosity level is verbose
   */
  def isVerbose: Boolean = {
    Output.VerbosityVerbose <= verbosity
  }

  /**
   * Check if this output's verbosity level is very verbose
   *
   * @return true if verbosity level is very verbose
   */
  def isVeryVerbose: Boolean = {
    Output.VerbosityVeryVerbose <= verbosity
  }

  /**
   * Check if this output's verbosity level is debug
   *
   * @return true if verbosity level is debug
   */
  def isDebug: Boolean = {
    Output.VerbosityDebug <= verbosity
  }

  /**
   * Write output with a new line appended
   *
   * @param message The message to write
   * @param mode The output mode
   * @param verbosity The message's verbosity to compare to the output verbosity
   */
  def writeln(
      message: String,
      mode: Int = Output.OutputNormal,
      verbosity: Int = Output.VerbosityNormal)
    : Unit = {

    write(message, newLine = true, mode, verbosity)
  }

  /**
   * Write output
   *
   * @param message The message to write
   * @param newLine Whether or not to append a new line on the end of the message
   * @param mode The output mode
   * @param verbosity The message's verbosity to compare to the output verbosity
   */
  def write(
      message: String,
      newLine: Boolean = false,
      mode: Int = Output.OutputNormal,
      verbosity: Int = Output.VerbosityNormal)
    : Unit = {

    if (verbosity <= this.verbosity) {
      val formatted = formatter.format(message, mode)

      doWrite(formatted, newLine)
    }
  }

  /**
   * Perform the actual writing
   *
   * @param message The message to write
   * @param newLine Whether or note to append a new line on the end of the message
   */
  protected def doWrite(message: String, newLine: Boolean): Unit
}

object Output {
  val VerbosityQuiet = 16
  val VerbosityNormal = 32
  val VerbosityVerbose = 64
  val VerbosityVeryVerbose = 128
  val VerbosityDebug = 256

  val OutputNormal = 1
  val OutputRaw = 2
  val OutputPlain = 4
}
