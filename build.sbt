lazy val commonSettings = Seq(
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.7"
)

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

lazy val server = project.enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      jdbc,
      cache,
      ws,
      "com.vmunier" %% "scalajs-scripts" % "1.0.0",
      "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
      "org.webjars" %% "webjars-play" % "2.5.0",
      "org.webjars" % "bootstrap" % "3.1.1-2",
      "org.webjars" % "animate.css" % "3.5.2"
    ),
    name := "play-scala",
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline)
  )
  .dependsOn(sharedJvm)

lazy val shared = crossProject.crossType(CrossType.Pure)
  .settings(commonSettings: _*)
  .settings(
      libraryDependencies ++= Seq(
          "com.lihaoyi" %%% "upickle" % "0.3.6",
          "com.lihaoyi" %%% "autowire" % "0.2.4",
          "com.lihaoyi" %%% "scalatags" % "0.5.2",
          "fr.hmil" %%% "roshttp" % "1.0.0"
      )
  )
  .jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val client = project.enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .settings(commonSettings: _*)
  .settings(
    persistLauncher := true,
    persistLauncher in Test := false,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1",
      "com.lihaoyi" %%% "autowire" % "0.2.4",
      "com.lihaoyi" %%% "upickle" % "0.3.6",
      "com.lihaoyi" %%% "scalatags" % "0.5.2",
      "be.doeraene" %%% "scalajs-jquery" % "0.9.1"
    )
).dependsOn(sharedJs)

lazy val populateCatalogueCache = taskKey[Unit]("Populate conf/example-hotels with some PCS properties")

populateCatalogueCache := {
  import collection.JavaConverters._,org.jsoup.Jsoup,scala.io.Source,util.Try
  for {
    destinationId <- Seq("de549499", "de504261", "de550392", "de554288")
    hotelId <- Jsoup.connect(s"https://uk.hotels.com/$destinationId/").get.select("a[data-hotel-id]").asScala.map(_.attr("data-hotel-id"))
    _ = println(s"Retrieve JSON for $hotelId in $destinationId")
    pcsResp <- Try(Source.fromURL(s"http://hcom-pcs-stg.staging-hotels.com/property_catalogue_svc/v2/property/$hotelId").mkString).toOption
    path = (resourceDirectory in Compile).value / "example-hotels" / s"$hotelId.json"
    _ = println(s"Writing to $path")
  } IO.write(path, pcsResp)
}