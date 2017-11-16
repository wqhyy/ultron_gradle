package com.xhs.qa.base

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

/**
  * Author：wuqihua on 2017/11/16 16:12
  */
object HttpCheckBase {
  val header_html = Map("Content-Type" -> "text/html")

  val header_json = Map("Content-Type" -> "application/json")

  val header_form = Map("Content-Type" -> "application/x-www-form-urlencoded")

  /**
    * http get json
    * @param apiName
    * @param localUrl
    * @return
    */
  def httpComGetJson(apiName: String, localUrl: String): HttpRequestBuilder = {

    val apiName_En = apiNameEncode(apiName, "GET")

    val httpCom = http(apiName_En)
      .get(localUrl)
      .headers(header_json)
      .check(status.is(200))

    httpCom
  }

  /**
    * http get html
    * @param apiName
    * @param localUrl
    * @return
    */
  def httpComGetHtml(apiName: String, localUrl: String): HttpRequestBuilder = {

    val apiName_En = apiNameEncode(apiName, "GET HTML")

    val httpCom = http(apiName_En)
      .get(localUrl)
      .headers(header_html)
      .check(status.is(200))

    httpCom

  }

  /**
    * api format
    *
    * @param apiName
    * @return
    */
  def apiNameEncode(apiName: String, method: String): String = {
    val apiNameSp = apiName.split("\\?",2)
    val apiNameApi = apiNameSp(0).replaceAll("[$]","").replaceAll("[{]", "<").replaceAll("[}]", ">") + " " + method + " api"

    apiNameApi
  }
}
