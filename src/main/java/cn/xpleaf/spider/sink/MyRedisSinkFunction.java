package cn.xpleaf.spider.sink;

import cn.xpleaf.spider.constants.SpiderConstants;
import cn.xpleaf.spider.core.pojo.UrlList;
import cn.xpleaf.spider.utils.JedisUtil;
import cn.xpleaf.spider.utils.SpiderUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author hong
 * @date 2022年08月20日 10:27
 */
public class MyRedisSinkFunction extends RichSinkFunction<UrlList> {

    Jedis jedis = null;

    public void open(Configuration parameters) throws Exception {
        jedis = JedisUtil.getJedis();
    }
    public void invoke(UrlList value, Context context) {
        List<String> high = value.getHighList();
        List<String> low = value.getLowList();
        if(!high.isEmpty()){
            for(String url:high){
                String domain = SpiderUtil.getTopDomain(url);   // 获取url对应的顶级域名，如jd.com
                String key = domain + SpiderConstants.SPIDER_DOMAIN_HIGHER_SUFFIX;            // 拼接url队列的key，如jd.com.higher
                jedis.lpush(key, url);
            }

        }
        if(!low.isEmpty()){
            for(String url:low){
                String domain = SpiderUtil.getTopDomain(url);   // 获取url对应的顶级域名，如jd.com
                String key = domain + SpiderConstants.SPIDER_DOMAIN_LOWER_SUFFIX;            // 拼接url队列的key，如jd.com.higher
                jedis.lpush(key, url);
            }

        }

    }
    public void close() throws Exception {
        super.close();
        if(jedis != null){
            JedisUtil.returnJedis(jedis);
        }
    }
}
