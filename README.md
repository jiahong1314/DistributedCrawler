# DistributedCrawler
字节青训营大数据项目-项目四 分布式爬虫系统   

本项目为基于流式计算框架flink开发的分布式爬虫系统，能够实现多线程并发爬取京东网站上商品信息，将爬取到的网页信息清洗解析并存储到数据库、且能够对数据进行检索的完整的爬虫功能。
### 1. 项目部署

#### 1.1 环境配置

```
Windows10系统
java：jdk1.8.0
flink：1.9.3
redis：验证redis-5.0.14可用
mysql：8.0.X
```

#### 1.2 启动步骤

1. 根据当前电脑redis和mysql配置，修改\resources\redis.properties和\resources\dbcp-config.properties配置文件。
2. 根据\scripts\db.sql建立相应的mysql数据库和表。
3. 进入redis安装目录，打开cmd，运行redis-server.exe，保持窗口开启。
4. 运行FlinkSpider.java，运行成功后结果如下图所示

```
-----flink url    https://list.jd.com/list.html?cat=9987,653,655&page=1
2022-08-22 11:37:49,663 [Flat Map -> Sink: Unnamed (1/4)#0] [cn.xpleaf.spider.utils.HttpUtil] [INFO] - 下载网页：https://list.jd.com/list.html?cat=9987,653,655&page=1，消耗时长：619 ms，代理信息：null:null
2022-08-22 11:37:49,844 [Flat Map -> Sink: Unnamed (1/4)#0] [cn.xpleaf.spider.core.parser.Impl.JDHtmlParserImpl] [INFO] - 解析列表页面:https://list.jd.com/list.html?cat=9987,653,655&page=1, 消耗时长:32ms
2022-08-22 11:37:49,844 [Flat Map -> Sink: Unnamed (1/4)#0] [cn.xpleaf.spider.ISpider] [INFO] - https://item.jd.com/100014352543.html
2022-08-22 11:37:49,844 [Flat Map -> Sink: Unnamed (1/4)#0] [cn.xpleaf.spider.ISpider] [INFO] - https://item.jd.com/100016034400.html
2022-08-22 11:37:49,845 [Flat Map -> Sink: Unnamed (1/4)#0] [cn.xpleaf.spider.ISpider] [INFO] - https://item.jd.com/100028235502.html
2022-08-22 11:37:49,845 [Flat Map -> Sink: Unnamed (1/4)#0] [cn.xpleaf.spider.ISpider] [INFO] - https://item.jd.com/100016931023.html
2022-08-22 11:37:49,845 [Flat Map -> Sink: Unnamed (1/4)#0] [cn.xpleaf.spider.ISpider] [INFO] - https://item.jd.com/100030441714.html
```

5. 一段时间后，运行SeedUrl.java，向redis数据库补充url



- 代理ip使用说明

在\resources\IPProxyRepository.txt中添加代理ip地址，格式如下：

```
47.106.105.236:80
222.66.202.6:80
122.226.57.70:8888
...
```

- 数据检索模块使用说明

运行Query.java，按照提示进行操作。





