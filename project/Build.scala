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

import sbt.Keys._
import sbt.{Build => BaseBuild, _}

import scala.io.Source

/**
 * Main build file
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object Build extends BaseBuild {

  import Dependencies._

  val projectVersion = Source.fromFile(file("./VERSION")).getLines().mkString.trim

  lazy val commonSettings = Seq(
    organization := "eidolon",
    publishMavenStyle := true,
    publishTo := Some(Resolver.file("release", new File("./release"))),
    resolvers ++= Dependencies.repositories,
    scalaVersion := "2.11.7",
    version := projectVersion,
    testOptions in Test += Tests.Argument("-oD")
  )

  lazy val console = (project in file("."))
    .settings(commonSettings: _*)
    .settings(name := "console")
    .settings(libraryDependencies ++=
      compile(chroma, scalaXml) ++
      test(mockito, scalaTest)
    )
}

