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

import eidolon.console.output.{ConsoleOutput, Output}

/**
 * FormatterMain
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object FormatterMain /*extends App*/ {
  val stdOut: Output = new ConsoleOutput()
  val stdErr: Output = stdOut match {
    case output: ConsoleOutput => output.errOutput
    case _ => stdOut
  }

  stdOut.write(
  """The <info>clone</info> command creates a clone of a remote repository
    |template and asks you questions to fill in template values.
    |
    |<info>$ eidola clone example/repo:gh .</info>""".stripMargin)

//  stdErr.write("<error>Looks like something fishy is going on, whatup homie?")
}
