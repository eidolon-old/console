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

package eidolon.console.output.formatter.exception

/**
 * Style Not Found Exception
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 *
 * @param style The style that was not found
 */
class StyleNotFoundException(
    style: String)
  extends Exception(
    ("Style '%s' not found. If you're trying to output XML-like strings via the formatter " +
      "remember to use the raw output mode.").format(style)
  )
