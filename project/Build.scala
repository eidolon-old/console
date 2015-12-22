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

import com.typesafe.sbt.packager.archetypes.{JavaAppPackaging, JavaServerAppPackaging}
import sbt.Keys._
import sbt.{Build => BaseBuild, _}

/**
 * Main build file
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object Build extends BaseBuild {

  import Dependencies._

  lazy val commonSettings = Seq(
    organization := "eidolon",
    publishMavenStyle := true,
    publishTo := Some(Resolver.sftp(
      "Eidolon Repo",
      "ssh.repo.eidolonframework.com",
      "/usr/share/nginx/html"
    )),
    scalaVersion := "2.11.7",
    version := "1.0.0",
    testOptions in Test += Tests.Argument("-oD")
  )

  lazy val console = (project in file("."))
    .enablePlugins(JavaAppPackaging)
    .enablePlugins(JavaServerAppPackaging)
    .settings(commonSettings: _*)
    .settings(name := "console")
    .settings(libraryDependencies ++=
      compile(chroma) ++
      test(mockito, scalaTest)
    )
}

