package cn.xpleaf.spider.core.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hong
 * @date 2022年08月20日 10:12
 */
public class UrlList implements Serializable {

    private List<String> highList;
    private List<String> lowList;

    public List<String> getHighList() {
        return highList;
    }

    public void setHighList(List<String> highList) {
        this.highList = highList;
    }

    public List<String> getLowList() {
        return lowList;
    }

    public void setLowList(List<String> lowList) {
        this.lowList = lowList;
    }
}
