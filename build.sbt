import Dependencies._

ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

enablePlugins(ModuxPlugin)
resolvers += Resolver.bintrayRepo("jsoft", "maven")

lazy val root = (project in file("."))
  .settings(
    name := "shop",
    moduxOpenAPIVersion := 3,
  )
