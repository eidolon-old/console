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
import java.io.PrintStream

/**
 * Output
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 * @param out The standard output stream (used by default)
 * @param err The error output stream
 */
class Output(
    val out: PrintStream,
    val err: PrintStream,
    val formatter: OutputFormatter,
    val verbosity: Int = Output.VerbosityNormal) {

  /**
   * Write a given message in a given stream
   *
   * @param message   The message to write
   * @param mode      The output mode
   * @param verbosity The message verbosity
   * @param stream    The stream to write to
   */
  def write(
      message: String,
      mode: Int = Output.ModeNormal,
      verbosity: Int = Output.VerbosityNormal,
      stream: PrintStream = this.out)
    : Unit = {

    if (verbosity <= this.verbosity) {
      stream.print(formatter.format(message, mode))
    }
  }

  /**
   * Write a given message with a new line appended
   *
   * @param message   The message to write
   * @param mode      The output mode
   * @param verbosity The message verbosity
   * @param stream    The stream to write to
   */
  def writeln(
      message: String,
      mode: Int = Output.ModeNormal,
      verbosity: Int = Output.VerbosityNormal,
      stream: PrintStream = this.out)
    : Unit = {

    if (verbosity <= this.verbosity) {
      stream.print(formatter.format(message, mode) + sys.props("line.separator"))
    }
  }

  /**
   * Check if this output's verbosity level is quiet
   *
   * @return true if verbosity level is quiet
   */
  def isQuiet: Boolean = {
    verbosity == Output.VerbosityQuiet
  }

  /**
   * Check if this output's verbosity includes quiet
   *
   * @return true if the verbosity level includes quiet
   */
  def hasQuiet: Boolean = {
    verbosity >= Output.VerbosityQuiet
  }

  /**
   * Check if this output's verbosity level is normal
   *
   * @return true if verbosity level is normal
   */
  def isNormal: Boolean = {
    verbosity == Output.VerbosityNormal
  }

  /**
   * Check if this output's verbosity level is normal
   *
   * @return true if verbosity level is normal
   */
  def hasNormal: Boolean = {
    verbosity >= Output.VerbosityNormal
  }

  /**
   * Check if this output's verbosity level is verbose
   *
   * @return true if verbosity level is verbose
   */
  def isVerbose: Boolean = {
    verbosity == Output.VerbosityVerbose
  }

  /**
   * Check if this output's verbosity level is verbose
   *
   * @return true if verbosity level is verbose
   */
  def hasVerbose: Boolean = {
    verbosity >= Output.VerbosityVerbose
  }

  /**
   * Check if this output's verbosity level is very verbose
   *
   * @return true if verbosity level is very verbose
   */
  def isVeryVerbose: Boolean = {
    verbosity == Output.VerbosityVeryVerbose
  }

  /**
   * Check if this output's verbosity level is very verbose
   *
   * @return true if verbosity level is very verbose
   */
  def hasVeryVerbose: Boolean = {
    verbosity >= Output.VerbosityVeryVerbose
  }

  /**
   * Check if this output's verbosity level is debug
   *
   * @return true if verbosity level is debug
   */
  def isDebug: Boolean = {
    verbosity == Output.VerbosityDebug
  }

  /**
   * Check if this output's verbosity level is debugß
   *
   * @return true if verbosity level is debug
   */
  def hasDebug: Boolean = {
    verbosity >= Output.VerbosityDebug
  }
}

/**
 * Output Companion
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object Output {
  val ModeNormal = 1
  val ModeRaw = 2
  val ModePlain = 4

  val VerbosityQuiet = 16
  val VerbosityNormal = 32
  val VerbosityVerbose = 64
  val VerbosityVeryVerbose = 128
  val VerbosityDebug = 256
}
