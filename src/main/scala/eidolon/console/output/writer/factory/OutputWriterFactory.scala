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

package eidolon.console.output.writer.factory

import eidolon.console.output.writer.OutputWriter

/**
 * OutputWriter Factory
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait OutputWriterFactory {
  /**
   * Build an output writer
   *
   * @param verbosity a verbosity level
   * @return an output writer
   */
  def build(verbosity: Int): OutputWriter
}
