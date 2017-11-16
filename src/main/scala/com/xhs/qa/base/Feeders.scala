package com.xhs.qa.base

import io.gatling.core.Predef._

/**
  * Author：wuqihua on 2017/11/16 15:50
  */
object Feeders {
  val manualFileEnvPrefixPath = ""

  val suitFolder = ReadEnv.readFolder("")

  val feeder_baidu_word = csv(manualFileEnvPrefixPath + suitFolder + "baidu_word.csv").circular

  val feeder_baidu_word_random = csv(manualFileEnvPrefixPath + suitFolder + "baidu_word.csv").random

  val feeder_baidu_word_queue = csv(manualFileEnvPrefixPath + suitFolder + "baidu_word.csv").queue

}
