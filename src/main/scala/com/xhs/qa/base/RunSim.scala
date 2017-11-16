package com.xhs.qa.base

import java.io.File

import com.typesafe.config.ConfigFactory
import io.gatling.app.Gatling
import io.gatling.core.Predef._
import io.gatling.core.config.GatlingFiles

/**
  * Author：wuqihua on 2017/11/16 17:19
  */
class RunSim extends Simulation{
  val slave_name = ReadEnv.readString("slavename")
  val tw_conf_path = "application.perf_conf."+slave_name+".tw"
  val cw_conf_path = "application.perf_conf."+slave_name+".cw"
  val du_conf_path = "application.perf_conf."+slave_name+".du"
  val sim_conf_path = "application.perf_conf."+slave_name+".sim"

  val configFile = ReadEnv.readString("config","config.conf")
  val config_path = GatlingFiles.dataDirectory + "/" + configFile
  System.out.println("tw conf file： "+config_path)
  val config = ConfigFactory.parseFile(new File(config_path))

  if (config.hasPath(sim_conf_path))
  {
    val sim_name = config.getString(sim_conf_path)

    if(System.getProperty("tw")==null&&config.hasPath(tw_conf_path))
    {
      val tw = config.getString(tw_conf_path)
      System.setProperty("tw",tw)
    }
    if(System.getProperty("cw")==null&&config.hasPath(cw_conf_path))
    {
      val cw = config.getString(cw_conf_path)
      System.setProperty("cw",cw)
    }
    if(System.getProperty("du")==null&&config.hasPath(du_conf_path))
    {
      val du = config.getString(du_conf_path)
      System.setProperty("du",du)
    }

    val paras= Array("-s",sim_name,"-df",GatlingFiles.dataDirectory.toString)
    Gatling.main(paras)
  }else
  {
    System.out.println("slave conf is empty, none simulation exec")
  }


}
