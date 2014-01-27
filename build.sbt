name := "heroku-play2-demo"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2",
  "com.newrelic.agent.java" % "newrelic-agent" % "3.4.2"
)     

play.Project.playScalaSettings