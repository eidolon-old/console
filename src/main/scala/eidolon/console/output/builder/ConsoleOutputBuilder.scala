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

package eidolon.console.output.builder

import eidolon.console.output.{ConsoleOutput, Output}

/**
 * ConsoleOutputBuilder
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleOutputBuilder extends OutputBuilder {
  override def build(): Output = {
    new ConsoleOutput()
  }
}
