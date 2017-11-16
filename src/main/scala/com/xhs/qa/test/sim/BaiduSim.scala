package com.xhs.qa.test.sim

/**
  * Author：wuqihua on 2017/11/16 16:31
  */
import com.xhs.qa.base.{DefaultSetup, Feeders}
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import com.xhs.qa.test.ob.BaiduOb._

import scala.concurrent.duration._

class BaiduSim extends Simulation{

  val baidu_s_ratio: Double = DefaultSetup.getRatio("application.ratio.baidu.s", 1)

  val scnBaiduS = scenario("baiduSearchTest")
    .feed(Feeders.feeder_baidu_word_random)
    .exec(httpBaiduSearchHtml).pause(1 seconds)

  val proportionMap: Map[ScenarioBuilder, Double] = Map(
    scnBaiduS -> baidu_s_ratio)
  DefaultSetup.defaultSetup(this, DefaultSetup.httpProtWithAuthHeader, proportionMap)

}
