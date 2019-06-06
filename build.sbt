lazy val root = Project("TestingSkinnyORM", file("."))
  .settings(
    scalaVersion := "2.12.8",
    organization := "org.airabinovich.testing",
    name := "TestingSkinnyORM",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-unchecked",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Xfuture",
      "-Ypartial-unification",
    ),
    libraryDependencies ++= Seq(
      "com.h2database" % "h2" % "1.4.+",
      "org.scalikejdbc" %% "scalikejdbc" % "3.3.2",
      "org.scalikejdbc" %% "scalikejdbc-config" % "3.3.2",
      "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.6.0-scalikejdbc-3.3",
      "org.skinny-framework" %% "skinny-orm" % "3.0.0",
      "com.zaxxer" % "HikariCP" % "3.3.1",
    ),
    initialCommands :=
      """
        |import scalikejdbc._
        |import skinny.orm._, feature._
        |import org.joda.time._
        |skinny.DBSettings.initialize()
        |implicit val session = AutoSession
      """.stripMargin
  )

