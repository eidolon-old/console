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
    verbosity == OutputWriter.VerbosityQuiet
  }

  /**
   * Check if this output's verbosity includes quiet
   *
   * @return true if the verbosity level includes quiet
   */
  def hasQuiet: Boolean = {
    verbosity >= OutputWriter.VerbosityQuiet
  }

  /**
   * Check if this output's verbosity level is normal
   *
   * @return true if verbosity level is normal
   */
  def isNormal: Boolean = {
    verbosity == OutputWriter.VerbosityNormal
  }

  /**
   * Check if this output's verbosity level is normal
   *
   * @return true if verbosity level is normal
   */
  def hasNormal: Boolean = {
    verbosity >= OutputWriter.VerbosityNormal
  }

  /**
   * Check if this output's verbosity level is verbose
   *
   * @return true if verbosity level is verbose
   */
  def isVerbose: Boolean = {
    verbosity == OutputWriter.VerbosityVerbose
  }

  /**
   * Check if this output's verbosity level is verbose
   *
   * @return true if verbosity level is verbose
   */
  def hasVerbose: Boolean = {
    verbosity >= OutputWriter.VerbosityVerbose
  }

  /**
   * Check if this output's verbosity level is very verbose
   *
   * @return true if verbosity level is very verbose
   */
  def isVeryVerbose: Boolean = {
    verbosity == OutputWriter.VerbosityVeryVerbose
  }

  /**
   * Check if this output's verbosity level is very verbose
   *
   * @return true if verbosity level is very verbose
   */
  def hasVeryVerbose: Boolean = {
    verbosity >= OutputWriter.VerbosityVeryVerbose
  }

  /**
   * Check if this output's verbosity level is debug
   *
   * @return true if verbosity level is debug
   */
  def isDebug: Boolean = {
    verbosity == OutputWriter.VerbosityDebug
  }

  /**
   * Check if this output's verbosity level is debugß
   *
   * @return true if verbosity level is debug
   */
  def hasDebug: Boolean = {
    verbosity >= OutputWriter.VerbosityDebug
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
