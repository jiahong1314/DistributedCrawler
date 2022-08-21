package cn.xpleaf.spider.redis;

import cn.xpleaf.spider.constants.SpiderConstants;
import cn.xpleaf.spider.utils.JedisUtil;
import cn.xpleaf.spider.utils.SpiderUtil;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @author hong
 * @date 2022年08月17日 19:47
 */
public class MyRedisSource implements SourceFunction<String> {
    private boolean isRunning =true;
    private Jedis jedis=null;
    private final long SLEEP_MILLION=5000;

    @Override
    public void run(SourceContext<String> sourceContext) throws Exception {
        this.jedis = new Jedis("127.0.0.1", 6379);
        while(isRunning){
            String url = null;
            while (url ==null){
                String randomDomain = jedis.srandmember(SpiderConstants.SPIDER_WEBSITE_DOMAINS_KEY);    // jd.com
                String key = randomDomain + SpiderConstants.SPIDER_DOMAIN_HIGHER_SUFFIX;                // jd.com.higher
                url = jedis.lpop(key);
                if(url == null) {   // 如果为null，则从低优先级中获取
                    key = randomDomain + SpiderConstants.SPIDER_DOMAIN_LOWER_SUFFIX;    // jd.com.lower
                    url = jedis.lpop(key);
                }
                SpiderUtil.sleep(5000);
            }


            //System.out.println("---Flink source url");
            sourceContext.collect(url);
            //JedisUtil.returnJedis(jedis);
        }
    }

    @Override
    public void cancel() {
        isRunning=false;
        while(jedis!=null){
            jedis.close();
        }
    }
}
