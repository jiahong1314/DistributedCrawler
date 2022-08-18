package cn.xpleaf.spider;

import cn.xpleaf.spider.core.download.impl.HttpGetDownloadImpl;
import cn.xpleaf.spider.core.parser.Impl.JDHtmlParserImpl;
import cn.xpleaf.spider.core.repository.impl.RandomRedisRepositoryImpl;
import cn.xpleaf.spider.core.store.impl.MySQLStoreImpl;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hong
 * @date 2022年08月17日 15:27
 */
public class SpiderMapFunction implements MapFunction<String , String> {
    private  ISpider iSpider;
    SpiderMapFunction(ISpider iSpider){
        this.iSpider = iSpider;
    }
    @Override
    public String map(String s) throws Exception {
        // 6.启动爬虫
        // iSpider.start();

        iSpider.startSingle(s);
        return null;
    }
}
