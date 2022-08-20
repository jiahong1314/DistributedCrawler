package cn.xpleaf.spider;

import cn.xpleaf.spider.client.MysqlClient;
import cn.xpleaf.spider.constants.SpiderConstants;
import cn.xpleaf.spider.core.download.IDownload;
import cn.xpleaf.spider.core.download.impl.HttpGetDownloadImpl;
import cn.xpleaf.spider.core.parser.IParser;
import cn.xpleaf.spider.core.parser.Impl.JDHtmlParserImpl;
import cn.xpleaf.spider.core.pojo.Page;
import cn.xpleaf.spider.core.pojo.UrlList;
import cn.xpleaf.spider.core.repository.IRepository;
import cn.xpleaf.spider.core.repository.impl.RandomRedisRepositoryImpl;
import cn.xpleaf.spider.core.store.IStore;
import cn.xpleaf.spider.core.store.impl.MySQLStoreImpl;
import cn.xpleaf.spider.utils.JedisUtil;
import cn.xpleaf.spider.utils.SpiderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 爬虫入口类
 */
public class ISpider implements Serializable {
    // log4j日志记录
    private Logger logger = LoggerFactory.getLogger(ISpider.class);
    // 爬虫下载器
    private IDownload download;
    // 爬虫解释器map: key-->需要爬取的网页的顶级域名     value-->该顶级域名的解释器实现类对象
    private Map<String, IParser> parsers = new HashMap<>();
    // 爬虫存储器
    private IStore store;
    // 域名高低优先级url标识器map: key-->domain   value-->Map<Level, url> 其中level为higher或lower即高低优先级的意思
    private Map<String, Map<String, String>> urlLevelMarker = new HashMap<>();
    // 域名列表
    private List<String> domains = new ArrayList<>();
    // 种子url
    /**
     * 这是初始启动爬虫程序时的种子url，当将该种子url的数据爬取完成后就没有数据爬取了
     * 那如何解决呢？这就需要使用我们的url调度系统，我们另外启动了一个url调度程序
     * 该程序会定时从redis的种子url列表中获取种子url，然后再添加到高优先级url列表中
     * 这样我们的爬虫程序就不会停下来，达到了定时爬取特别网页数据的目的
     */
    private List<String> seedUrls = new ArrayList<>();
    // url仓库
    private IRepository repository;

    //单例模式
    private static ISpider iSpider;
    private ISpider(){}

    public static synchronized ISpider getInstance(){
        if(iSpider == null){
            iSpider = new ISpider();
            // 1.注入下载器
            iSpider.setDownload(new HttpGetDownloadImpl());
            // 2.注入解析器
            // 2.1 添加需要爬取的域名列表
            List<String> domains = new ArrayList<>();
            domains.add("jd.com");
            iSpider.setDomains(domains);
            // 2.2 注入解析器
            iSpider.setParsers("jd.com", new JDHtmlParserImpl());
            // 2.2 设置高低优先级url标识器
            Map<String, String> jdMarker = new HashMap<>();
            jdMarker.put("higher", "https://list.jd.com/");
            jdMarker.put("lower", "https://item.jd.com");

            iSpider.setUrlLevelMarker("jd.com", jdMarker);


            // 3.注入存储器
            iSpider.setStore(new MySQLStoreImpl());

            // 4.设置种子url
            iSpider.setSeedUrls("https://list.jd.com/list.html?cat=9987,653,655&page=1");
            // 5.设置url仓库
            //RandomRedisRepositoryImpl repository = new RandomRedisRepositoryImpl();
            iSpider.setRepository(new RandomRedisRepositoryImpl()); // 设置url仓库
        }
        return iSpider;
    }
    /**
     * 完成网页数据的下载
     *
     * @param url
     * @return
     */
    public Page download(String url) {
        return this.download.download(url);
    }

    /**
     * 根据不同的域名，选择不同的解析器对下载的网页进行解析
     *
     * @param page
     */
    public void parser(Page page, String domain) {
        IParser domainParser = this.parsers.get(domain);
        if (domainParser != null) {
            domainParser.parser(page);
        } else {
            logger.error("没有对应域名{}的解析器", domain);
        }
    }

    /**
     * 存储解析之后的网页数据信息
     *
     * @param page
     */
    public void store(Page page) {
        this.store.store(page);
    }

    public  UrlList startSingle(String url){
        UrlList urlList = new UrlList();
        String domain = SpiderUtil.getTopDomain(url);   // 获取url对应的顶级域名
        System.out.println("-----flink url"+"    "+url);
        if (url != null) {  // 从url仓库中获取的url不为null
            // 下载网页
            Page page = download(url);
            // 解析网页
            if (page.getContent() != null) { // 只有content不为null时才进行后面的操作，否则没有意义
                List<String> highList = new ArrayList<>();
                List<String> lowList = new ArrayList<>();
                parser(page, domain); // 如果该url为列表url，从这里有可能解析出很多的url
                for (String pUrl : page.getUrls()) { // 向url仓库中添加url
                    logger.info(pUrl);
                    String higherUrlMark = urlLevelMarker.get(domain).get("higher");
                    String lowerUrlMark = urlLevelMarker.get(domain).get("lower");
                    if (pUrl.startsWith(higherUrlMark)) {    // 高优先级
                        //repository.offerHigher(pUrl);
                        highList.add(pUrl);
                    } else if (pUrl.startsWith(lowerUrlMark)) { // 低优先级
                        //repository.offerLower(pUrl);
                        lowList.add(pUrl);
                    }
                }
                urlList.setHighList(highList);
                urlList.setLowList(lowList);
                if (page.getId() != null) {  // 当商品id不为null时，说明前面解析的url是商品url，而不是列表url，这时存储数据才有意义
                    // 存储解析数据
                    // store(page);
                    // System.out.println(page);
                    //
                    MysqlClient.insert(page);
                }
            }
            // 上面操作结束之后必须要休息一会，否则频率太高的话很有可能会被封ip
            //SpiderUtil.sleep(1000);
        } else {    // 从url仓库中没有获取到url
            logger.info("没有url，请及时添加种子url");
            SpiderUtil.sleep(2000);
        }

        return urlList;
    }


    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setDownload(IDownload download) {
        this.download = download;
    }

    /**
     * 添加网页解析器
     *
     * @param domain 域名
     * @param parser 该域名对应的具体解析器实现类对象
     */
    public void setParsers(String domain, IParser parser) {
        this.parsers.put(domain, parser);
    }

    public void setStore(IStore store) {
        this.store = store;
    }

    public void setSeedUrls(String seedUrl) {
        this.seedUrls.add(seedUrl);
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
        for (String seedUrl : this.seedUrls) {   // 添加种子url到url仓库中
            this.repository.offerHigher(seedUrl);
        }
    }

    /**
     * 设置域名高低优先级url标识器
     *
     * @param domain 域名
     * @param map    域名的高低优先级url解析器Map<level, url>
     */
    public void setUrlLevelMarker(String domain, Map<String, String> map) {
        this.urlLevelMarker.put(domain, map);
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
        // 同时，为了方便操作，也将域名添加到redis的spider.website.domains集合中，这样就不用手动在redis中添加
        Jedis jedis = JedisUtil.getJedis();
        jedis.del(SpiderConstants.SPIDER_WEBSITE_DOMAINS_KEY);
        for(String domain : domains) {
            jedis.sadd(SpiderConstants.SPIDER_WEBSITE_DOMAINS_KEY, domain);
        }
        JedisUtil.returnJedis(jedis);
    }
}
