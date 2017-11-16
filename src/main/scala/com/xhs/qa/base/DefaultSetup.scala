package com.xhs.qa.base

import java.io.File

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.core.config.GatlingFiles
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocol
import scala.concurrent.duration._

import scala.collection.mutable.ListBuffer

/**
  * Author：wuqihua on 2017/11/16 15:44
  */
object DefaultSetup {

  def debugSession = exec { session => {
    val isDebug = ReadEnv.readInt(ReadEnv.debug, 0)
    if (0 != isDebug) {
      println(session)
    }
  }
    session
  }

  val httpProtWithAuthHeader = http
    .baseURL("https://www.baidu.com")
    .acceptEncodingHeader("gzip, deflate")
    .shareConnections


  def defaultSetup(simulation: Simulation, httpProtocol: HttpProtocol, proportionMap: Map[ScenarioBuilder, Double]): Unit = {

    val isrun = ReadEnv.readString("isrun", "true").toBoolean

    if (isrun) {
      val cw = ReadEnv.readInt("cw", 100)
      val tw = ReadEnv.readInt("tw", 1000)
      val du = ReadEnv.readLong("du", 60)

      val exct = ReadEnv.readString("excl", "excl")

      val constUsers = tw * cw / 100

      var proportionMapNew: Map[ScenarioBuilder, Double] = Map()

      if (!"excl".equals(exct)) {
        val exctList = exct.split(",", 20)
        proportionMap.keys.foreach(builder => {
          if (!exctList.contains(builder.name)) {
            proportionMapNew += (builder -> proportionMap(builder))
          }
        })
      } else {
        proportionMapNew = proportionMap
      }

      val dumax = (du * 2).toInt
      scnsSetup(simulation, constUsers, du, httpProtocol, proportionMapNew).maxDuration(dumax seconds)
    }
  }


  def scnsSetup(simulation: Simulation, constUsers: Double, duringTime: Double, httpProtocol: HttpProtocol, proportionMap: Map[ScenarioBuilder, Double]): simulation.SetUp = {

    println("[Xiaohongshu Gatling Team] The constUsers are : " + constUsers)
    println("[Xiaohongshu Gatling Team] The during Time is : " + duringTime)
    println("*********************************************************")
    proportionMap.keys.foreach { builder =>
      println("\"" + builder.name + "\"" + "相对占比: " + proportionMap(builder) + ",所以users = " + (constUsers * proportionMap(builder)).toInt)
    }
    val scenarioBuilderList = ListBuffer[PopulationBuilder]()
    proportionMap.keys.foreach { builder =>
      scenarioBuilderList += scnInject(constUsers, duringTime, httpProtocol, builder, -999, proportionMap(builder))
    }

    val simSetup = simulation.setUp(scenarioBuilderList.toList)

    simSetup

  }

  def scnInject(constUsers: Double, duringTime: Double, httpProtocol: HttpProtocol, scn: ScenarioBuilder, cp: Int = 0, ratio: Double = 1): PopulationBuilder = {
    var users = ReadEnv.readUsers(constUsers.toInt)
    users = (users * ratio).toInt
    var time = ReadEnv.readTime(duringTime.toInt)
    if (users < 1) {
      users = 1
    }
    var startUsers = (users * 0.1).toInt
    if (startUsers < 1) {
      startUsers = 1
    }
    if (time < 1) {
      time = 1
    }
    val rps = users.toInt
    var _cp = ReadEnv.readCp(cp)
    if (_cp == -999) {
      _cp = Integer.getInteger("cp", 1).intValue()
    }

    var reachTime = time * 0.3
    if (reachTime > 30)
      reachTime = 30
    val holdTime = time - reachTime
    if (_cp > 0 && holdTime > 0) {
      println("use throttle for ramp")
      scn.inject(
        rampUsersPerSec(startUsers) to (users) during (reachTime seconds),
        constantUsersPerSec(users) during (time seconds),
        nothingFor(reachTime seconds)
      )
        .throttle(
          reachRps(rps) in (reachTime seconds),
          holdFor(time seconds)
        )
        .protocols(httpProtocol)
    } else if (_cp == -1) {
      println("use constantUsersPerSec(users) during (time seconds)")
      scn.inject(
        constantUsersPerSec(users) during (time seconds)
      ).protocols(httpProtocol)
    } else {
      println("use rampUsersPerSec for ramp")
      scn.inject(
        rampUsersPerSec(startUsers) to (users) during (reachTime seconds),
        constantUsersPerSec(users) during (time seconds),
        nothingFor(reachTime seconds)
      ).protocols(httpProtocol)
    }
  }

  val configFile = ReadEnv.readString("config", "config.conf")
  val configPath = GatlingFiles.dataDirectory + "/" + configFile
  val config = ConfigFactory.parseFile(new File(configPath))

  def getRatio(ratioName: String, defaultRatio: Double): Double = {
    var ratio = defaultRatio
    if (ratioName != "" && ratioName != " ") {
      try {
        ratio = config.getDouble(ratioName)
      } catch {
        case ex: Exception => ratio = defaultRatio
          println("can not find path in conf:" + ratioName)
      }
    }
    if (ratio < 0) {
      ratio = 0
    }
    ratio
  }
}
