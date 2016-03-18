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

package eidolon.console.issue18


/**
 * Input Model
 */

trait InputModel

case class ExampleInputModel(
    source: String,
    destination: String,
    clean: Boolean)
  extends InputModel


/**
 * Input Model Builder
 */

trait InputModelBuider[M] {
  def build(): M
}

class ExampleInputModelBuilder
  extends InputModelBuider[ExampleInputModel] {

  var _source: String = _
  var _destination: String = _
  var _clean: Boolean = _

  def source = _source
  def destination = _destination
  def clean = _clean

  def source_=(value: String) = _source = value
  def destination_=(value: String) = _destination = value
  def clean_=(value: Boolean) = _clean = value

  def build(): ExampleInputModel = {
    new ExampleInputModel(source, destination, clean)
  }
}


/**
 * Input Definition
 */

class InputDefinition[M <: InputModel, B <: InputModelBuider[M]](val options: List[InputOption[M, B]] = List()) {
  def withOption(name: String, default: Option[String], mapper: (B, String) => Unit): InputDefinition[M, B] = {
    new InputDefinition[M, B] (
      options :+ new InputOption[M, B](name, default, mapper)
    )
  }
}


/**
 * Input Option
 */

case class InputOption[M <: InputModel, B <: InputModelBuider[M]](
    name: String,
    default: Option[String],
    mapper: (B, String) => Unit)


/**
 * Input Readers
 */

object InputReaders {
  def readInt(input: String): Int = input.toInt
  def readString(input: String): String = input
  def readDouble(input: Double): Double = input.toDouble
  def readBoolean(input: String): Boolean = {
    input.toLowerCase match {
      case "true" => true
      case "false" => false
      case "y" => true
      case "n" => false
      case "yes" => true
      case "no" => false
      case "1" => true
      case "0" => false
      case invalidInput =>
        throw new Exception(s"'$invalidInput' is not a boolean.")
    }
  }
}


case class ParsedInputLongOption(
    token: String,
    value: String)


/**
 * Main
 */

object Main {
  import InputReaders._

  def apply(): Unit = {
    val builder = new ExampleInputModelBuilder()
    val definition = new InputDefinition[ExampleInputModel, ExampleInputModelBuilder]()
      .withOption("source", None, (builder, input) => builder.source = readString(input))
      .withOption("destination", Some("."), (builder, input) => builder.destination = readString(input))
      .withOption("clean", Some("false"), (builder, input) => builder.clean = readBoolean(input))

    // What happens when you comment these out, and have empty input? We get null. That cannot
    // happen. Default values are Options, and as such if they're None it implies there is no
    // default value, not that the default value is None. How do we specify defaults for other
    // types? Should there even be something that does that?
    // Perhaps, if something can be left blank it should be wrapped in Option, and should default
    // to None in the model? By blank, that would mean something that is optional, and has no
    // default value.
    // What do we do about simple flags with no values? How do we read those? This seems to me like
    // it's also why we have the value of parsed options wrapped in Options, because they _can_ be
    // None, i.e. no value.
    // Simple flags should ideally be Booleans, that'd be Unit type ones here I guess?
    // How do you specify a default value for a simple flag?
    // Should the default values have to be strings? It'd be good for them to be typed properly to
    // cater for these interesing problems.
    val input: List[ParsedInputLongOption] = List(
      new ParsedInputLongOption("source", "gh:eidolon/console"),
      new ParsedInputLongOption("destination", "."),
      new ParsedInputLongOption("clean", "false")
    )

    definition.options.foreach { option =>
      input.find(_.token == option.name) match {
        case Some(opt) => option.mapper(builder, opt.value)
        case _ => option.default match {
          case Some(default) => option.mapper(builder, default)
          case _ => // No value could be assigned, should we be throwing here?
        }
      }
    }

    val model = builder.build()

    println(model)
  }
}
