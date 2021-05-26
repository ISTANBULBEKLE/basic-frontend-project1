name := """basic-frontend-project1"""
organization := "hmrc"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.5"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

libraryDependencies += ws

libraryDependencies +="org.mockito" % "mockito-core" % "2.28.2" % Test
libraryDependencies += ehcache

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "hmrc.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "hmrc.binders._"
