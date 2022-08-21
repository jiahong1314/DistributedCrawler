package cn.xpleaf.spider;

import cn.xpleaf.spider.constants.SpiderConstants;
import cn.xpleaf.spider.utils.JedisUtil;
import cn.xpleaf.spider.utils.SpiderUtil;
import redis.clients.jedis.Jedis;

/**
 * @author hong
 * @date 2022年08月20日 17:00
 */
public class SeedUrl {

    public static void main(String[] args) {
        Jedis jedis = JedisUtil.getJedis();
        for (int i = 3; i < 50; i+=2) {
            String url = "https://list.jd.com/list.html?cat=9987,653,655&page="+i;
            String domain = SpiderUtil.getTopDomain(url);   // 获取url对应的顶级域名，如jd.com
            String key = domain + SpiderConstants.SPIDER_DOMAIN_HIGHER_SUFFIX;            // 拼接url队列的key，如jd.com.higher
            jedis.lpush(key, url);
            SpiderUtil.sleep(1000);
        }
        JedisUtil.returnJedis(jedis);
    }
}
