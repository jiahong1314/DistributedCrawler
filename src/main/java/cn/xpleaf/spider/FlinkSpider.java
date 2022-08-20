package cn.xpleaf.spider;

import cn.xpleaf.spider.core.download.impl.HttpGetDownloadImpl;
import cn.xpleaf.spider.core.parser.Impl.JDHtmlParserImpl;
import cn.xpleaf.spider.core.pojo.UrlList;
import cn.xpleaf.spider.core.repository.impl.RandomRedisRepositoryImpl;
import cn.xpleaf.spider.core.store.impl.MySQLStoreImpl;
import cn.xpleaf.spider.map.SpiderFlatMapFunction;
import cn.xpleaf.spider.redis.MyRedisSource;
import cn.xpleaf.spider.sink.MyRedisSinkFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hong
 * @date 2022年08月17日 14:41
 */
public class FlinkSpider {
    public static void main(String[] args) throws Exception {
        ISpider iSpider = ISpider.getInstance();

        // 创建流式执行环境
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);

        // 添加数据源
        DataStreamSource<String> stream = env.addSource(new MyRedisSource());

        // 处理数据 SingleOutputStreamOperator<String> map =
        //stream.map(new SpiderMapFunction(iSpider));
        SingleOutputStreamOperator<UrlList> urlListSingleOutputStreamOperator = stream.flatMap(new SpiderFlatMapFunction(iSpider));

        urlListSingleOutputStreamOperator.addSink(new MyRedisSinkFunction());
        env.execute();
    }
}
