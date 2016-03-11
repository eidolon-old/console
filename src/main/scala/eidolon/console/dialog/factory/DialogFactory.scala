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

package eidolon.console.dialog.factory

import eidolon.console.dialog.Dialog
import eidolon.console.output.formatter.OutputFormatter
import jline.console.ConsoleReader
import jline.Terminal
import jline.TerminalFactory

/**
 * Console Dialog Factory
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class DialogFactory(formatter: OutputFormatter) {
  /**
   * Build a dialog instance
   *
   * @return the built dialog
   */
  def build(): Dialog = {
    usingTerminal { _ =>
      new Dialog(
        formatter,
        new ConsoleReader(
          System.in,
          System.out
        )
      )
    }
  }

  /**
   * Initialize a terminal to disable echo, and use this terminal in the given callback
   *
   * @param callback a function to run with the terminal
   * @tparam T the return type of the callback
   * @return the return value of the callback
   */
  private def withTerminal[T](callback: Terminal => T): T = {
    synchronized {
      val terminal = TerminalFactory.get()

      terminal.synchronized {
        callback(terminal)
      }
    }
  }

  /**
   * Used to access a terminal object
   *
   * @param callback a function to run with the terminal
   * @tparam T the return type of the callback
   * @return the return value of the callback
   */
  private def usingTerminal[T](callback: Terminal => T): T = {
    withTerminal { terminal =>
      terminal.restore()

      callback(terminal)
    }
  }
}
