package com.atb.crawler.task;

import com.atb.crawler.entity.Movie;
import com.atb.crawler.service.ActressService;
import com.atb.crawler.service.MovieService;
import com.atb.crawler.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 呆呆
 * @Datetime 2021/11/27 13:03
 */
@Component
public class MovieTask {
    @Autowired
    private MovieService movieService;


    // 解析页面, 获取商品数据并存储
    /*private void parse(String html) throws Exception {
        // 解析html获取Document对象
        Document doc = Jsoup.parse(html);
        // 获取spu信息
        Elements spuEles = doc.select("div#J_goodsList > ul > li");

        for (Element spuEle : spuEles) {
            // 排除没有data-spu的值的广告
            if (StringUtils.isNotEmpty(spuEle.attr("data-spu"))) {
                // 获取spu
                long spu = Long.parseLong(spuEle.attr("data-spu"));
                // 获取sku信息
                Elements skuEles = spuEle.select("li.ps-item");
                for (Element skuEle : skuEles) {
                    // 获取sku
                    long sku = Long.parseLong(skuEle.select("[data-sku]").first().attr("data-sku"));
                    // 根据sku查询商品数据
                    Item item = new Item();
                    item.setSku(sku);
                    List<Item> list = this.itemService.findAll(item);
                    if (list.size() > 0) {
                        // 如果商品存在, 就进行下一个循环, 该商品不保存, 因为已存在
                        continue;
                    }
                    // 设置商品的spu
                    item.setSpu(spu);
                    // 获取商品的详情的url
                    String itemUrl = "https://item.jd.com/" + sku + ".html";
                    item.setUrl(itemUrl);
                    // 获取商品的图片
                    String picUrl = "https:" + skuEle.select("img[data-sku]").first().attr("data-lazy-img");
                    picUrl = picUrl.replace("/n7/", "/n0/" );
                    String picName = this.httpUtils.doGetImage(picUrl);
                    item.setPic(picName);
                    // 获取商品的价格
                    String priceJson = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + sku);
                    double price = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
                    item.setPrice(price);
                    // 获取商品的标题
                    String itemInfo = this.httpUtils.doGetHtml(item.getUrl());
                    String title = Jsoup.parse(itemInfo).select("div.sku-name").text();
                    item.setTitle(title);
                    //item.setTitle();
                    item.setCreated(new Date());
                    item.setUpdated(item.getCreated());

                    // 保存商品数据到数据库中
                    this.itemService.save(item);
                }
            }

        }
    }*/
}
