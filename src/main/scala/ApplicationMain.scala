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

import eidolon.console.Application
import eidolon.console.command.CloneCommand

/**
 * Main
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object ApplicationMain extends App {
  def time[A](f: => A) = {
    val s = System.nanoTime
    val ret = f

    println("Time taken: " + (System.nanoTime - s) / 1e6 + "ms")

    ret
  }

  time {
    val app = Application("eidolon/console", "0.1.0-SNAPSHOT")
      .withCommand(new CloneCommand())

    app.run(args.toList)
  }
}
