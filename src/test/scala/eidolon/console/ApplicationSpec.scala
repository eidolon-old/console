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

package eidolon.console

import java.io.PrintStream

import eidolon.console.command.Command
import eidolon.console.dialog.Dialog
import eidolon.console.dialog.factory.DialogFactory
import eidolon.console.input.Input
import eidolon.console.input.definition.InputDefinition
import eidolon.console.input.definition.parameter.{InputOption, InputArgument}
import eidolon.console.input.factory.InputFactory
import eidolon.console.input.parser.InputParser
import eidolon.console.input.validation.InputValidator
import eidolon.console.output.Output
import eidolon.console.output.factory.OutputFactory
import eidolon.console.output.formatter.OutputFormatter
import eidolon.console.output.writer.factory.OutputWriterFactory
import eidolon.console.output.writer.{PrintStreamOutputWriter, OutputWriter}
import org.mockito.AdditionalAnswers._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

/**
 * Application Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ApplicationSpec extends FunSpec with BeforeAndAfter with MockitoSugar {
  var baseApplication: Application = _
  val name: String = "eidolon/console"
  val version: String = "1.2.3-test"
  var outputBuffer: StringBuilder = _
  var inputParser: InputParser = _
  var inputValidator: InputValidator = _
  var inputFactory: InputFactory = _
  var outputFactory: OutputFactory = _
  var dialogFactory: DialogFactory = _
  var input: Input = _
  var dialog: Dialog = _
  var outputFormatter: OutputFormatter = _
  var outputWriterFactory: OutputWriterFactory = _
  var outputWriterStream: PrintStream = _

  before {
    outputBuffer = new StringBuilder()
    inputParser = new InputParser()
    inputValidator = new InputValidator()
    inputFactory = new InputFactory()
    dialogFactory = mock[DialogFactory]
    dialog = mock[Dialog]
    outputFormatter = mock[OutputFormatter]
    outputWriterFactory = mock[OutputWriterFactory]
    outputWriterStream = mock[PrintStream]

    when(outputFormatter.format(anyString(), anyInt())).thenAnswer(returnsFirstArg())
    when(outputWriterStream.print(anyString()))
      .thenAnswer(new Answer[Unit] {
        override def answer(invocation: InvocationOnMock): Unit = {
          outputBuffer.append(invocation.getArgumentAt(0, classOf[String]))
        }
      })

    when(outputWriterFactory.build(anyInt()))
      .thenAnswer(new Answer[OutputWriter] {
        override def answer(invocation: InvocationOnMock): OutputWriter = {
          new PrintStreamOutputWriter(
            outputFormatter,
            outputWriterStream,
            invocation.getArgumentAt(0, classOf[Int])
          )
        }
      })

    when(dialogFactory.build()).thenReturn(dialog)

    outputFactory = new OutputFactory(outputWriterFactory, outputWriterFactory)
    baseApplication = new Application(
      name,
      version,
      inputParser,
      inputValidator,
      inputFactory,
      outputFactory,
      dialogFactory
    )
  }

  describe("eidolon.console.Application") {
    describe("run()") {
      it("should output the list of available commands by default") {
        val application = baseApplication

        application.run(Array[String]())

        val result = outputBuffer.toString

        assert(result.contains("Available commands:"))
        assert(result.contains("help"))
        assert(result.contains("list"))
      }

      it("should output command help if a command is misused") {
        val application = baseApplication
          .withCommand(new Command {
            override val name: String = "test"
            override val definition: InputDefinition = new InputDefinition()
              .withArgument("reqArg", mode = InputArgument.REQUIRED, description = Some("foobar"))
              .withOption("reqOpt", mode = InputOption.VALUE_REQUIRED, description = Some("bazqux"))

            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
          })

        application.run(List("test"))

        val result = outputBuffer.toString

        assert(result.contains("Usage:"))
        assert(result.contains("reqArg"))
        assert(result.contains("reqOpt"))
        assert(result.contains("foobar"))
        assert(result.contains("bazqux"))
      }

      it("should output command help if it is requested via the global option") {
        val application = baseApplication
          .withCommand(new Command {
            override val name: String = "test"
            override val definition: InputDefinition = new InputDefinition()
              .withArgument("reqArg", description = Some("foobar"))
              .withOption("reqOpt", description = Some("bazqux"))

            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
          })

        application.run(List("test", "--help"))

        val result = outputBuffer.toString

        assert(result.contains("Usage:"))
        assert(result.contains("reqArg"))
        assert(result.contains("reqOpt"))
        assert(result.contains("foobar"))
        assert(result.contains("bazqux"))
      }

      it("should run a command if it exists and is valid") {
        val application = baseApplication
          .withCommand(new Command {
            override val name: String = "test"
            override val definition: InputDefinition = new InputDefinition()
              .withArgument("reqArg", mode = InputArgument.REQUIRED)
              .withOption("reqOpt", mode = InputOption.VALUE_NONE)

            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
              output.out.writeln("this command ran successfully")
            }
          })

        application.run(List("test", "--reqOpt", "test"))

        val result = outputBuffer.toString

        assert(result.contains("this command ran successfully"))
      }

      it("should run a command if it exists and is valid by one of it's aliases") {
        val application = baseApplication
          .withCommand(new Command {
            override val name: String = "test"
            override val aliases: List[String] = List("testalias")
            override val definition: InputDefinition = new InputDefinition()
              .withArgument("reqArg", mode = InputArgument.REQUIRED)
              .withOption("reqOpt", mode = InputOption.VALUE_NONE)

            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
              output.out.writeln("this command ran successfully")
            }
          })

        application.run(List("testalias", "--reqOpt", "test"))

        val result = outputBuffer.toString

        assert(result.contains("this command ran successfully"))
      }

      it("should show quiet messages if the quiet option is set") {
        val application = baseApplication
          .withCommand(new Command {
            override val name: String = "test"

            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
              output.out.writeln("this message is visible", verbosity = OutputWriter.VerbosityQuiet)
            }
          })

        application.run(List("test", "--quiet"))

        val result = outputBuffer.toString

        assert(result.contains("this message is visible"))
      }

      it("should show quiet messages if the quiet option is not set") {
        val application = baseApplication
          .withCommand(new Command {
            override val name: String = "test"

            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
              output.out.writeln("this message is visible", verbosity = OutputWriter.VerbosityQuiet)
            }
          })

        application.run(List("test"))

        val result = outputBuffer.toString

        assert(result.contains("this message is visible"))
      }

      it("should show debug messages if the verbose option is set to 3") {
        val application = baseApplication
          .withCommand(new Command {
            override val name: String = "test"

            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
              output.out.writeln("this message is visible", verbosity = OutputWriter.VerbosityDebug)
            }
          })

        application.run(List("test", "--verbose=3"))

        val result = outputBuffer.toString

        assert(result.contains("this message is visible"))
      }

      it("should show very verbose messages if the verbose option is set to 2") {
        val application = baseApplication
          .withCommand(new Command {
            override val name: String = "test"

            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
              output.out.writeln("this message is visible", verbosity = OutputWriter.VerbosityVeryVerbose)
            }
          })

        application.run(List("test", "--verbose=2"))

        val result = outputBuffer.toString

        assert(result.contains("this message is visible"))
      }

      it("should show verbose messages if the verbose option is set to 1") {
        val application = baseApplication
          .withCommand(new Command {
            override val name: String = "test"

            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
              output.out.writeln("this message is visible", verbosity = OutputWriter.VerbosityVerbose)
            }
          })

        application.run(List("test", "--verbose=1"))

        val result = outputBuffer.toString

        assert(result.contains("this message is visible"))
      }

      it("should not show verbose messages if the verbose option is not set") {
        val application = baseApplication
          .withCommand(new Command {
            override val name: String = "test"

            override def execute(input: Input, output: Output, dialog: Dialog): Unit = {
              output.out.writeln("this message is visible", verbosity = OutputWriter.VerbosityVerbose)
            }
          })

        application.run(List("test"))

        val result = outputBuffer.toString

        assert(!result.contains("this message is visible"))
      }
    }

    describe("withCommand()") {
      it("should add a command to the application") {
        val command = new Command {
          override val name: String = "test"
          override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
        }

        val application = baseApplication.withCommand(command)

        assert(baseApplication != application)
        assert(!baseApplication.commands.contains(command))
        assert(application.commands.contains(command))
      }
    }

    describe("withCommands()") {
      it("should add several commands to the application") {
        val command1 = new Command {
          override val name: String = "test1"

          /**
           * Execute this command
           *
           * @param input  Input passed to the application
           * @param output Output interface for displaying formatted text
           */
          override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
        }

        val command2 = new Command {
          override val name: String = "test2"
          override def execute(input: Input, output: Output, dialog: Dialog): Unit = {}
        }

        val application = baseApplication.withCommands(List(command1, command2))

        assert(baseApplication != application)
        assert(!baseApplication.commands.contains(command1))
        assert(!baseApplication.commands.contains(command2))
        assert(application.commands.contains(command1))
        assert(application.commands.contains(command2))
      }
    }
  }
}
