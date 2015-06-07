import sbt._
import Keys._

object StackableControllerProjects extends Build {

  lazy val _organization = "jp.t2v"

  lazy val _version = "0.5.0-SNAPSHOT"

  def _publishTo(v: String) = {
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")  
    else Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }

  lazy val _resolvers = Seq(
    "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
    "sonatype releases" at "http://oss.sonatype.org/content/repositories/releases"
  )

  lazy val _scalacOptions = Seq("-unchecked")

  lazy val _pomExtra = {
    <url>https://github.com/t2v/stackable-controller</url>
    <licenses>
      <license>
        <name>Apache License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:stackable-controller</url>
      <connection>scm:git:git@github.com:stackable-controller</connection>
    </scm>
    <developers>
      <developer>
        <id>gakuzzzz</id>
        <name>gakuzzzz</name>
        <url>https://github.com/gakuzzzz</url>
      </developer>
    </developers>
  }

  lazy val core = Project(
    id = "core", 
    base = file("core")
  ).settings(
    organization := _organization,
    name := "stackable-controller",
    version := _version,
    scalaVersion := "2.11.6",
    crossScalaVersions := "2.11.6" :: "2.10.5" :: Nil,
    publishTo <<= version { (v: String) => _publishTo(v) },
    publishMavenStyle := true,
    resolvers ++= _resolvers,
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play" % play.core.PlayVersion.current % "provided"
    ),
    sbtPlugin := false,
    scalacOptions ++= _scalacOptions,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { x => false },
    pomExtra := _pomExtra
  )

  lazy val sample = Project("sample", file("sample")).enablePlugins(play.sbt.PlayScala).settings(
    version := _version,
    scalaVersion := "2.11.6",
    resolvers ++= _resolvers,
    libraryDependencies ++= Seq(
      play.sbt.Play.autoImport.jdbc,
      play.sbt.Play.autoImport.specs2 % "test",
      "com.typesafe.play"  %% "play"                      % play.core.PlayVersion.current,
      "org.scalikejdbc"    %% "scalikejdbc"               % "2.2.7",
      "org.scalikejdbc"    %% "scalikejdbc-config"        % "2.2.7",
      "org.scalikejdbc"    %% "scalikejdbc-play-initializer" % "2.4.0",
      "org.slf4j"          %  "slf4j-simple"              % "[1.7,)"
    )
  ) dependsOn(core)

  lazy val root = Project(id = "root", base = file(".")).settings(
    scalaVersion := "2.11.6"
  ).aggregate(core, sample) 

}
