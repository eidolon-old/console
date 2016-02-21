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

package eidolon.console.output.writer

import eidolon.console.output.formatter.OutputFormatter

/**
 * Output Writer
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait OutputWriter {
  val formatter: OutputFormatter
  val verbosity: Int = OutputWriter.VerbosityNormal

  /**
   * Write a given message
   *
   * @param message The message to write
   * @param mode The output mode
   * @param verbosity The message verbosity
   */
  def write(
      message: String,
      mode: Int = OutputWriter.ModeNormal,
      verbosity: Int = OutputWriter.VerbosityNormal)
    : Unit

  /**
   * Write a given message with a new line appended
   *
   * @param message The message to write
   * @param mode The output mode
   * @param verbosity The message verbosity
   */
  def writeln(
      message: String,
      mode: Int = OutputWriter.ModeNormal,
      verbosity: Int = OutputWriter.VerbosityNormal)
    : Unit

  /**
   * Check if this output's verbosity level is quiet
   *
   * @return true if verbosity level is quiet
   */
  def isQuiet: Boolean = {
    OutputWriter.VerbosityQuiet == verbosity
  }

  /**
   * Check if this output's verbosity level is verbose
   *
   * @return true if verbosity level is verbose
   */
  def isVerbose: Boolean = {
    OutputWriter.VerbosityVerbose <= verbosity
  }

  /**
   * Check if this output's verbosity level is very verbose
   *
   * @return true if verbosity level is very verbose
   */
  def isVeryVerbose: Boolean = {
    OutputWriter.VerbosityVeryVerbose <= verbosity
  }

  /**
   * Check if this output's verbosity level is debug
   *
   * @return true if verbosity level is debug
   */
  def isDebug: Boolean = {
    OutputWriter.VerbosityDebug <= verbosity
  }
}

object OutputWriter {
  val ModeNormal = 1
  val ModeRaw = 2
  val ModePlain = 4

  val VerbosityQuiet = 16
  val VerbosityNormal = 32
  val VerbosityVerbose = 64
  val VerbosityVeryVerbose = 128
  val VerbosityDebug = 256
}
