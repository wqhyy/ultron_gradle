package com.xhs.qa.test.ob

/**
  * Author：wuqihua on 2017/11/16 16:23
  */

import com.xhs.qa.base.HttpCheckBase._

object BaiduOb {

  val urlSearchHtml = "/s?wd=${s_wd}"

  val httpBaiduSearchHtml = httpComGetHtml(urlSearchHtml, urlSearchHtml)

}
