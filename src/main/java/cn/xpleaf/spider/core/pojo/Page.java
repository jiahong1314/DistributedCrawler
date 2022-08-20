package cn.xpleaf.spider.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

/**
 * 网页对象，主要包含网页内容和商品数据
 */
public class Page implements Serializable {
    private String content;              // 网页内容

    private String id;                    // 商品Id
    private String source;               // 商品来源
    private String brand;                // 商品品牌
    private String title;                // 商品标题
    private Float price;                // 商品价格
    private String commentCount;        // 商品评论数

    private Float goodRate;
    private String url;                  // 商品地址
    private String imgUrl;             // 商品图片地址
    private String params;              // 商品规格参数

    private Map paramDetails;       // 商品规格参数分解信息

    @Override
    public String toString() {
        return "Page{" +
                "content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", brand='" + brand + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", commentCount=" + commentCount +
                ", url='" + url + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", params='" + params + '\'' +
                ", urls=" + urls +
                '}';
    }

    private List<String> urls = new ArrayList<>();  // 解析列表页面时用来保存解析的商品url的容器

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public Float getGoodRate() {
        return goodRate;
    }

    public void setGoodRate(Float goodRate) {
        this.goodRate = goodRate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setParamDetails(Map paramDetails) {
        this.paramDetails = paramDetails;
    }

    public Map getParamDetails() {
        return paramDetails;
    }

    public List<String> getUrls() {
        return urls;
    }
}
