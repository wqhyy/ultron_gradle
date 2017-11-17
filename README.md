# Guide

ultron_gradle是一个基于Gatling和Gradle的压测工具，能够帮助性能测试的同学们，快速进行压力测试。该工具执行压力测试，只需要一条简单的java命令即可。

# QuickStart

```
./gradlew fatCapsule
java -Dtw=100 -Dcw=1 -Ddu=10 -Dexcl=excl -jar build/libs/ultron_gradle-1.0-SNAPSHOT-capsule.jar -s com.xhs.qa.test.sim.BaiduSim
```
# 参数含义
| 参数名 | 含义 |
| -------- | -------- |
| -Dtw   | 用户总数   |
| -Dcw   | 用户占比，设置为1的时候表示用户数为tw的百分之1   |
| -Ddu   | 执行时间，秒级   |
| -Dexcl | 需要排除的场景，填写scenario的名字，多个以英文逗号分隔 |
| -Dfolder | 测试套件的概念，填写数据文件路径下的目录名 |
| -s | 需要执行的脚本 |
| -df | 数据文件路径(conf和csv文件) |