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

package eidolon.console.dialog.builder

import eidolon.console.dialog.{ConsoleDialog, Dialog}

/**
 * ConsoleDialogBuilder
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ConsoleDialogBuilder extends DialogBuilder {
  /**
   * @inheritdoc
   */
  override def build(): Dialog = {
    new ConsoleDialog()
  }
}