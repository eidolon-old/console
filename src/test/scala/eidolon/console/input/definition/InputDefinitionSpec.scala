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

package eidolon.console.input.definition

import eidolon.console.input.definition.parameter.{InputOption, InputArgument}
import org.scalatest.FunSpec

/**
 * InputDefinition Spec
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputDefinitionSpec extends FunSpec {
  describe("eidolon.console.input.definition.InputDefinition") {
    describe("++()") {
      it("should return a new input definition that's a combination of the arguments and options " +
        "of two other input definitions") {

        val definition1 = new InputDefinition(arguments = List(new InputArgument("arg")))
        val definition2 = new InputDefinition(options = List(new InputOption("opt")))

        val result = definition1 ++ definition2

        assert(result != definition1)
        assert(result != definition2)
        assert(result.hasArgument("arg"))
        assert(result.hasOption("opt"))
      }
    }

    describe("getArgument()") {
      it("should return an argument in the definition with the given name when one exists") {
        val definition = new InputDefinition(arguments = List(new InputArgument("arg")))

        assert(definition.arguments.nonEmpty)
        assert(definition.getArgument("arg").nonEmpty)
      }

      it("should return None if no argument exists in the definition with the given name") {
        val definition = new InputDefinition()

        assert(definition.arguments.isEmpty)
        assert(definition.getArgument("arg").isEmpty)
      }
    }

    describe("getArgumentAtIndex()") {
      it("should return an argument in the definition at the given index when one exists") {
        val definition = new InputDefinition(arguments = List(new InputArgument("arg")))

        assert(definition.arguments.nonEmpty)
        assert(definition.getArgumentAtIndex(0).nonEmpty)
      }

      it("should return None if no argument exists in the definition at the given index") {
        val definition = new InputDefinition()

        assert(definition.arguments.isEmpty)
        assert(definition.getArgumentAtIndex(0).isEmpty)
      }
    }

    describe("hasArgument()") {
      it("should return true if the definition contains an argument with the given name") {
        val definition = new InputDefinition(arguments = List(new InputArgument("arg")))

        assert(definition.arguments.nonEmpty)
        assert(definition.hasArgument("arg"))
      }

      it("should return false if the definition doesn't contain an argument with the given name") {
        val definition = new InputDefinition()

        assert(definition.arguments.isEmpty)
        assert(!definition.hasArgument("arg"))
      }
    }

    describe("hasArgumentAtIndex()") {
      it("should return true if the definition contains an argument at the given index") {
        val definition = new InputDefinition(arguments = List(new InputArgument("arg")))

        assert(definition.arguments.nonEmpty)
        assert(definition.hasArgumentAtIndex(0))
      }

      it("should return false if the definition doesn't contain an argument at the given index") {
        val definition = new InputDefinition()

        assert(definition.arguments.isEmpty)
        assert(!definition.hasArgumentAtIndex(0))
      }
    }

    describe("getOption()") {
      it("should return an option in the definition with the given name when one exists") {
        val definition = new InputDefinition(options = List(new InputOption("opt")))

        assert(definition.options.nonEmpty)
        assert(definition.getOption("opt").nonEmpty)
      }

      it("should return None if no option exists in the definition with the given name") {
        val definition = new InputDefinition()

        assert(definition.options.isEmpty)
        assert(definition.getOption("opt").isEmpty)
      }
    }

    describe("getOptionByShortName()") {
      it("should return an option in the definition with the given short name when one exists") {
        val definition = new InputDefinition(options = List(new InputOption(
          name = "opt",
          shortName = Some("o")
        )))

        assert(definition.options.nonEmpty)
        assert(definition.getOptionByShortName("o").nonEmpty)
      }

      it("should return None if no option exists in the definition with the given short name") {
        val definition = new InputDefinition()

        assert(definition.options.isEmpty)
        assert(definition.getOptionByShortName("o").isEmpty)
      }
    }

    describe("hasOption()") {
      it("should return true if the definition contains an option with the given name") {
        val definition = new InputDefinition(options = List(new InputOption("opt")))

        assert(definition.options.nonEmpty)
        assert(definition.hasOption("opt"))
      }

      it("should return false if the definition doesn't contain an option with the given name") {
        val definition = new InputDefinition()

        assert(definition.options.isEmpty)
        assert(!definition.hasOption("opt"))
      }
    }

    describe("withArgument()") {
      describe("when passing an instance of InputArgument") {
        it("should return a new instance of the definition with the given argument added") {
          val definition = new InputDefinition()
          val argument = new InputArgument(
            name = "arg",
            mode = InputArgument.OPTIONAL,
            description = Some("argument description"),
            default = Some("argument default")
          )

          val result = definition.withArgument(argument)

          assert(definition.arguments.isEmpty)
          assert(result.arguments.nonEmpty)
          assert(result.arguments.contains(argument))
        }
      }

      describe("when passing all parameters manually") {
        it("should return a new instance of the definition with the given argument added") {
          val definition = new InputDefinition()
          val result = definition.withArgument(
            name = "arg",
            mode = InputArgument.OPTIONAL,
            description = Some("argument description"),
            default = Some("argument default")
          )

          assert(definition.arguments.isEmpty)
          assert(result.arguments.nonEmpty)
          assert(result.arguments.exists(_.name == "arg"))
        }

        it("should create the argument with the given name") {
          val definition = new InputDefinition()
          val name = "arg"
          val result = definition.withArgument(
            name = name,
            mode = InputArgument.OPTIONAL,
            description = Some("argument description"),
            default = Some("argument default")
          )

          assert(result.arguments.size == 1)
          assert(result.arguments.exists(_.name == name))
        }

        it("should create the argument with the given mode") {
          val definition = new InputDefinition()
          val mode = InputArgument.OPTIONAL
          val result = definition.withArgument(
            name = "arg",
            mode = mode,
            description = Some("argument description"),
            default = Some("argument default")
          )

          assert(result.arguments.size == 1)
          assert(result.arguments.exists(_.mode == mode))
        }

        it("should create the argument with the given description") {
          val definition = new InputDefinition()
          val description = Some("argument description")
          val result = definition.withArgument(
            name = "arg",
            mode = InputArgument.OPTIONAL,
            description = description,
            default = Some("argument default")
          )

          assert(result.arguments.size == 1)
          assert(result.arguments.exists(_.description == description))
        }

        it("should create the argument with the given default") {
          val definition = new InputDefinition()
          val default = Some("argument default")
          val result = definition.withArgument(
            name = "arg",
            mode = InputArgument.OPTIONAL,
            description = Some("argument description"),
            default = default
          )

          assert(result.arguments.size == 1)
          assert(result.arguments.exists(_.default == default))
        }
      }
    }

    describe("withOption()") {
      describe("when passing an instance of InputOption") {
        it("should return a new instance of the definition with the given option added") {
          val definition = new InputDefinition()
          val option = new InputOption(
            name = "opt",
            shortName = Some("o"),
            mode = InputOption.VALUE_OPTIONAL,
            description = Some("option description"),
            defaultValue = Some("option default")
          )

          val result = definition.withOption(option)

          assert(definition.options.isEmpty)
          assert(result.options.nonEmpty)
          assert(result.options.contains(option))
        }
      }

      describe("when passing all parameters manually") {
        it("should return a new instance of the definition with the given option added") {
          val definition = new InputDefinition()
          val result = definition.withOption(
            name = "opt",
            shortName = Some("o"),
            mode = InputOption.VALUE_OPTIONAL,
            description = Some("option description"),
            defaultValue = Some("option default")
          )

          assert(definition.options.isEmpty)
          assert(result.options.nonEmpty)
          assert(result.options.exists(_.name == "opt"))
        }

        it("should create the option with the given name") {
          val definition = new InputDefinition()
          val name = "opt"
          val result = definition.withOption(
            name = name,
            shortName = Some("o"),
            mode = InputOption.VALUE_OPTIONAL,
            description = Some("option description"),
            defaultValue = Some("option default")
          )

          assert(result.options.size == 1)
          assert(result.options.exists(_.name == name))
        }

        it("should create the option with the given short name") {
          val definition = new InputDefinition()
          val shortName = Some("o")
          val result = definition.withOption(
            name = "opt",
            shortName = shortName,
            mode = InputOption.VALUE_OPTIONAL,
            description = Some("option description"),
            defaultValue = Some("option default")
          )

          assert(result.options.size == 1)
          assert(result.options.exists(_.shortName == shortName))
        }

        it("should create the option with the given mode") {
          val definition = new InputDefinition()
          val mode = InputOption.VALUE_OPTIONAL
          val result = definition.withOption(
            name = "opt",
            shortName = Some("o"),
            mode = mode,
            description = Some("option description"),
            defaultValue = Some("option default")
          )

          assert(result.options.size == 1)
          assert(result.options.exists(_.mode == mode))
        }

        it("should create the option with the given description") {
          val definition = new InputDefinition()
          val description = Some("option description")
          val result = definition.withOption(
            name = "opt",
            shortName = Some("o"),
            mode = InputOption.VALUE_OPTIONAL,
            description = Some("option description"),
            defaultValue = Some("option default")
          )

          assert(result.options.size == 1)
          assert(result.options.exists(_.description == description))
        }

        it("should create the option with the given default") {
          val definition = new InputDefinition()
          val defaultValue = Some("option default")
          val result = definition.withOption(
            name = "opt",
            shortName = Some("o"),
            mode = InputOption.VALUE_OPTIONAL,
            description = Some("option description"),
            defaultValue = defaultValue
          )

          assert(result.options.size == 1)
          assert(result.options.exists(_.defaultValue == defaultValue))
        }
      }
    }
  }
}
