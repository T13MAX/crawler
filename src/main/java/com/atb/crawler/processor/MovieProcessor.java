package com.atb.crawler.processor;


import com.atb.crawler.entity.Actress;
import com.atb.crawler.entity.Movie;
import com.atb.crawler.pipeline.MoviePipeline;
import com.atb.crawler.service.ActressService;
import com.atb.crawler.service.MovieService;
import com.atb.crawler.utils.HttpUtils;
import com.atb.crawler.utils.SpringUtil;
import javafx.application.Application;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author 呆呆
 * @Datetime 2021/11/27 19:39
 */
@Component
public class MovieProcessor implements PageProcessor {

    private static String PRE = "https://www.vipfanhao.com/";
    private static String FAN = "fan/";
    private static String YOU = "you/";
    private static String HTML = ".html";
    private static DateFormat FMT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private MoviePipeline moviePipeline;
    @Autowired
    private ActressService actressService;

    @Override
    public void process(Page page) {

        List<String> all = page.getHtml().css("div[class=gallery-list list-box] div ul").links().all();
        Set<String> set = new HashSet<>(all);//去重
        //判断是否是库里没有的新作品
        for (String s : set) {
            String number = s.substring(s.indexOf("fan/") + 4, s.lastIndexOf(".html"));
            Movie movie = SpringUtil.getBean(MovieService.class).findByNumber(number);
            if (movie == null) page.addTargetRequest(s);
        }
        //page.addTargetRequests(new ArrayList<>(set));
        saveMovie(page);
    }

    private void saveMovie(Page page) {
        Document doc = Jsoup.parse(page.getHtml().toString());
        Elements movieDetailList = doc.select("div[class=text-box loadimg]").select("p");
        if (movieDetailList.size() == 0) return;//为0说明是作品列表页 直接return
        Map<String, String> map = new HashMap<>();
        for (Element element : movieDetailList) {
            map.put(element.select("strong").text(), element.text());
        }
        Movie movie = new Movie();
        String photoUrl = movieDetailList.select("img").attr("src");
        movie.setMoviename(map.get("作品名：").substring(4));
        movie.setNumber(map.get("番号：").substring(3));
        movie.setName(map.get("主演：").substring(3));
        movie.setLabel(map.get("类别标签：").substring(5));
        movie.setPhoto(HttpUtils.doGetImage(photoUrl, movie.getNumber(), movie.getName()));
        movie.setPhoto(photoUrl);
        movie.setGrade(8);//刚入库
        movie.setCreatedate(new Date());
        Date date;
        try {
            date = FMT.parse(map.get("发行日期：").substring(5));
        } catch (ParseException e) {
            date = new Date(148204915200000L);
        }
        movie.setDate(date);
        page.putField("movie", movie);
    }

    private Site site = Site.me()
            .setCharset("utf8")
            .setTimeOut(10 * 1000)
            .setRetrySleepTime(3000)
            .setSleepTime(3);

    @Override
    public Site getSite() {
        return site;
    }

    //@Scheduled(initialDelay = 1000, fixedDelay = 100000 * 1000)
    public void run() {
        System.out.println("爬虫开始了");
        //HttpClientDownloader downloader = new HttpClientDownloader();//https://proxy.mimvp.com/freeopen
        //"27.105.130.93",8080)));
        //downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("221.226.94.218", 110)));
        List<Actress> allActress = actressService.findAllActress();
        String[] strs = new String[allActress.size()];
        for (int i = 0; i < allActress.size(); i++) {
            strs[i] = PRE + YOU + allActress.get(i).getUrlname() + HTML;
        }
        Spider.create(new MovieProcessor())
                .addUrl(strs)
                .addPipeline(moviePipeline)
                .thread(3)
                //.setDownloader(downloader)
                .run();
        System.out.println("爬虫结束了");
    }
}
