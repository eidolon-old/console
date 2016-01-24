import eidolon.chroma.Chroma
import eidolon.console.output.formatter.ConsoleOutputFormatter
import eidolon.console.output.formatter.lexer.OutputFormatLexer

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


/**
 * FormatterMain
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object FormatterMain extends App {
//  val formatter = new ConsoleOutputFormatter(Chroma())
//
//  println(formatter.format("OL <info>IL <error>IIL</error> I <error>IIR</error> IR</info> OR"))

  val lexer = new OutputFormatLexer()
  val tokens = lexer.tokenise("<something> <info>that</info> <<< >>> < < > < ><  has some <error>content</error> in")

  println(tokens)
}
