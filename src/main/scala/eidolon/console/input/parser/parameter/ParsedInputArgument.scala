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

package eidolon.console.input.parser.parameter

/**
 * Parsed Input Argument
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 *
 * @param token An input argument token
 */
case class ParsedInputArgument(
    token: String)
  extends ParsedInputParameter
