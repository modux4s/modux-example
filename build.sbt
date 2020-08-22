import Dependencies._

ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

enablePlugins(ModuxPlugin)
resolvers += Resolver.mavenLocal

lazy val root = (project in file("."))
  .settings(
    name := "shop",
    moduxOpenAPIVersion := 3,
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.9.1"
  )
