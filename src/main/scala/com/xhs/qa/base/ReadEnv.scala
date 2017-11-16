package com.xhs.qa.base

/**
  * Author：wuqihua on 2017/11/16 15:46
  */
object ReadEnv {

  val debug = "debug"
  val feeder = "df"
  val cp = "cp"
  val folder = "folder"
  val users = "users"
  val time = "time"
  var params = "params"

  /** *******************************************************************************
    * 先读特定环境变量，再读通用环境变量，最后才是脚本设定默认值
    * *********************************************************************************/

  def readString(envName: String, defaultValue: String = ""): String = {
    var envValue = System.getProperty(envName)
    if (envValue == null || envValue == "") {
      envValue = readParams(envName, defaultValue)
    }
    envValue
  }

  def readInt(envName: String, defaultValue: Int): Int = {
    var envValue = Integer.getInteger(envName)
    if (envValue == null) {
      val valueStr = readParams(envName)
      if (valueStr != null && valueStr != "") {
        envValue = valueStr.toInt
      }
    }
    if (envValue == null) {
      envValue = defaultValue
    }
    envValue.toInt
  }

  def readLong(envName: String, defaultValue: Long): Long = {
    var envValue = java.lang.Long.getLong(envName)
    if (envValue == null) {
      val valueStr = readParams(envName)
      if (valueStr != null && valueStr != "") {
        envValue = valueStr.toLong
      }
    }
    if (envValue == null) {
      envValue = defaultValue
    }
    envValue.toLong
  }

  def readParams(envName: String, defaultValue: String = ""): String = {
    var paramsStr = System.getProperty(params)
    if (paramsStr != null && paramsStr != "") {
      paramsStr = paramsStr.replace("\"", "");
      val params = paramsStr.split(",,").toList
      params.foreach { param =>
        val keyValuePair = param.split("::").toList
        if (keyValuePair.length > 1) {
          val key = keyValuePair(0)
          val value = keyValuePair(1)
          println("key:" + key + "value:" + value)
          if (key == envName) {
            return value
          }
        }
      }
      defaultValue
    } else {
      defaultValue
    }
  }

  /** *******************************************************************************
    * 所有待读环境变量
    * *********************************************************************************/
  def readUsers(defaultUsers: Int = 0): Int = {
    readInt(ReadEnv.users, defaultUsers)
  }

  def readTime(defaultTime: Long = 0): Long = {
    readLong(ReadEnv.time, defaultTime)
  }

  def readCp(defaultCp: Int): Int = {
    readInt(ReadEnv.cp, defaultCp)
  }

  //read Feeder from suits
  def readFolder(defaultFolder: String): String = {
    readString(ReadEnv.folder, defaultFolder)
  }

}
