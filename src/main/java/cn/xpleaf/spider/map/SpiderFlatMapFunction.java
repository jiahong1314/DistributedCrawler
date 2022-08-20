package cn.xpleaf.spider.map;

import cn.xpleaf.spider.ISpider;
import cn.xpleaf.spider.core.pojo.UrlList;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * @author hong
 * @date 2022年08月20日 9:15
 */
public class SpiderFlatMapFunction implements FlatMapFunction<String, UrlList> {
    private ISpider iSpider;
    public SpiderFlatMapFunction(ISpider iSpider){
        this.iSpider = iSpider;
    }
    @Override
    public void flatMap(String s, Collector<UrlList> collector) throws Exception {
        UrlList urlList = iSpider.startSingle(s);
        collector.collect(urlList);
    }
}
