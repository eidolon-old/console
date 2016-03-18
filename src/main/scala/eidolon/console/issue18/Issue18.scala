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

class InputDefinition[M <: InputModel, B <: InputModelBuider[M]](val options: List[InputOption[M, B, _]] = List()) {
  def withBooleanOption(name: String, default: Option[Boolean], mapper: (B, Boolean) => Unit): InputDefinition[M, B] = {
    new InputDefinition[M, B] (
      options :+ new BooleanInputOption[M, B](name, default, mapper)
    )
  }

  def withIntOption(name: String, default: Option[Int], mapper: (B, Int) => Unit): InputDefinition[M, B] = {
    new InputDefinition[M, B] (
      options :+ new IntInputOption[M, B](name, default, mapper)
    )
  }

  def withStringOption(name: String, default: Option[String], mapper: (B, String) => Unit): InputDefinition[M, B] = {
    new InputDefinition[M, B] (
      options :+ new StringInputOption[M, B](name, default, mapper)
    )
  }
}


/**
 * Input Option
 */

trait InputOption[M <: InputModel, B <: InputModelBuider[M], V] {
    val name: String
    val default: Option[V]
    val mapper: (B, V) => Unit
}

case class BooleanInputOption[M <: InputModel, B <: InputModelBuider[M]](
    override val name: String,
    override val default: Option[Boolean],
    override val mapper: (B, Boolean) => Unit)
  extends InputOption[M, B, Boolean]

case class IntInputOption[M <: InputModel, B <: InputModelBuider[M]](
    override val name: String,
    override val default: Option[Int],
    override val mapper: (B, Int) => Unit)
  extends InputOption[M, B, Int]

case class StringInputOption[M <: InputModel, B <: InputModelBuider[M]](
    override val name: String,
    override val default: Option[String],
    override val mapper: (B, String) => Unit)
  extends InputOption[M, B, String]

/**
 * Input Readers
 */

trait InputReader[V] {
  val reads: String => V
}

object InputReader {
  def reads[V](f: String => V): InputReader[V] = new InputReader[V] {
    val reads = f
  }

  implicit val intRead: InputReader[Int] = reads[Int](_.toInt)
  implicit val stringRead: InputReader[String] = reads[String](identity)
  implicit val doubleRead: InputReader[Double] = reads[Double](_.toDouble)
  implicit val booleanRead: InputReader[Boolean] = reads[Boolean]({
    _.toLowerCase match {
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
  })
}

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

trait ParameterReader
class OptionReader extends ParameterReader {
  def read[V](option: InputOption[_, _, V], value: String)(implicit reader: InputReader[V]): V = {
    implicitly[InputReader[V]].reads(value)
  }
}

/**
 * Input Parameters
 */

case class ParsedInputLongOption(
    token: String,
    value: Option[String])


/**
 * Input Parser
 */

class InputParser[M <: InputModel, B <: InputModelBuider[M]](
    builder: B,
    definition: InputDefinition[M, B]) {

  import InputReaders._

  def parse(input: List[ParsedInputLongOption]): M = {
    definition.options.foreach(defOpt => {
      val maybeItem = input.find(_.token == defOpt.name)

      maybeItem match {
        case Some(item) => defOpt match {
          case booleanOpt: BooleanInputOption[M, B] => item.value match {
            // Input has value
            case Some(y) =>
              booleanOpt.mapper(builder, readBoolean(y))

            // Input has no value
            case _ => booleanOpt.default match {
              case Some(z) => booleanOpt.mapper(builder, z)
              case _ => // ???
            }
          }

          case intOpt: IntInputOption[M, B] => item.value match {
            // Input has value
            case Some(y) =>
              intOpt.mapper(builder, readInt(y))

            // Input has no value
            case _ => intOpt.default match {
              case Some(z) => intOpt.mapper(builder, z)
              case _ => // ???
            }
          }

          case stringOpt: StringInputOption[M, B] => item.value match {
            // Input has value
            case Some(y) =>
              stringOpt.mapper(builder, readString(y))

            // Input has no value
            case _ => stringOpt.default match {
              case Some(z) => stringOpt.mapper(builder, z)
              case _ => // ???
            }
          }

          case _ =>
            // @todo: Throw?
        }
        case _ => defOpt match {
          case booleanOpt: BooleanInputOption[M, B] => booleanOpt.default match {
            case Some(z) => booleanOpt.mapper(builder, z)
            case _ => // ???
          }

          case intOpt: IntInputOption[M, B] => intOpt.default match {
            case Some(z) => intOpt.mapper(builder, z)
            case _ => // ???
          }

          case stringOpt: StringInputOption[M, B] => stringOpt.default match {
            case Some(z) => stringOpt.mapper(builder, z)
            case _ => // ???
          }
        }
      }
    })

    builder.build()
  }
}


/**
 * Main
 */

object Main {
  def apply(): Unit = {
    val builder = new ExampleInputModelBuilder()
    val definition = new InputDefinition[ExampleInputModel, ExampleInputModelBuilder]()
      .withStringOption("source", None, (builder, input) => builder.source = input)
      .withStringOption("destination", Some("~/.tests"), (builder, input) => builder.destination = input)
      .withBooleanOption("clean", Some(false), (builder, input) => builder.clean = input)

    val input: List[ParsedInputLongOption] = List(
      new ParsedInputLongOption("source", Some("gh:eidolon/console"))
//      new ParsedInputLongOption("destination", Some(".")),
//      new ParsedInputLongOption("clean", Some("false"))
    )

    val parser = new InputParser[ExampleInputModel, ExampleInputModelBuilder](builder, definition)
    val model = parser.parse(input)

    println(model)
  }
}
