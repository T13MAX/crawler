package com.atb.crawler.service;

import com.atb.crawler.dao.MovieDao;
import com.atb.crawler.entity.Actress;
import com.atb.crawler.entity.Movie;
import com.atb.crawler.task.MovieThread;
import com.atb.crawler.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author 呆呆
 * @Datetime 2021/11/27 12:52
 */
@Service
public class MovieService {

    @Autowired
    private MovieDao movieDao;
    @Autowired
    private ActressService actressService;

    private static String PRE = "https://www.vipfanhao.com/";
    private static String FAN = "fan/";
    private static String YOU = "you/";
    private static String HTML = ".html";
    private static DateFormat FMT = new SimpleDateFormat("yyyy-MM-dd");

    public static CopyOnWriteArrayList<Actress> TASK_LIST;

    public void saveMovie(Movie movie) {
        movieDao.save(movie);
        System.out.println(movie.getName() + "的" + movie.getNumber() + "保存成功");
    }

    public void saveMovieList(List<Movie> movieList) {
        movieDao.saveAll(movieList);
    }

    public List<Movie> findAllMovie() {
        return movieDao.findAll();
    }

    public Movie findByNumber(String number){
       return movieDao.findByNumber(number);
    }

    public void movieTask() {
        HttpUtils.init();
         //System.out.println(HttpUtils.doGetHtml("https://www.vipfanhao.com"));
        List<Actress> allActress = actressService.findAllActress();
        Actress actress = allActress.get(1);
        //for (Actress actress : allActress) {
        System.out.println("开始爬取" + actress.getName() + "的数据");
        String urlName = actress.getUrlname();
        String url = PRE + YOU + urlName + HTML;
        String html = HttpUtils.doGetHtml(url);
        List<String> numberList = getNumberList(html);
        //if (numberList.size() == 0) continue;
        getMovieList(numberList.subList(0, 3));
        System.out.println(actress.getName() + "的数据抓取完成");




        /*TASK_LIST = new CopyOnWriteArrayList();
        //加入任务队列
        for (Actress actress : allActress) {
            TASK_LIST.add(actress);
        }
        //起三个个线程
        for (int i = 0; i < 2; i++) {
           new MovieThread().start();
        }*/

    }

    /**
     * 先去演员专属页爬取所有番号
     *
     * @return java.util.List<java.lang.String>
     * @Author 呆呆
     * @Date 2021/11/27 13:24
     * @Param [html]
     **/
    public List<String> getNumberList(String html) {
        List<String> result = new ArrayList<>();
        // 解析html获取Document对象
        Document doc = Jsoup.parse(html);
        // 获取spu信息
        Elements movieList = doc.select("div[class=gallery-list list-box]").select("div").select("ul").select("li");
        for (Element element : movieList) {
            Elements a = element.select("a");
            String href = a.attr("href");//  /fan/SSIS-270.html
            String[] split = href.split("\\/");
            //System.out.println(split1[0]);
            if (split.length == 3) {
                String[] split1 = split[2].split("\\.");
                if (split1.length == 2) {
                    result.add(split1[0]);
                }
            }
        }
        System.out.println("爬取所有番号成功,一共" + result.size() + "部作品");
        return result;
    }

    /**
     * 通过番号列表爬取对应作品的信息
     *
     * @return java.util.List<com.atb.crawler.entity.Movie>
     * @Author 呆呆
     * @Date 2021/11/27 14:49
     * @Param [numberList]
     **/
    public List<Movie> getMovieList(List<String> numberList) {
        System.out.println("开始获取前十部具体信息");
        List<Movie> movieList = new ArrayList<>();

        for (String str : numberList) {
            Movie movie = getMovie(str);
            movieList.add(movie);
        }
        System.out.println("前十部获取完成");
        return movieList;
    }

    /**
     * 根据番号获取作品
     *
     * @return com.atb.crawler.entity.Movie
     * @Author 呆呆
     * @Date 2021/11/27 19:01
     * @Param [number]
     **/
    public Movie getMovie(String number) {
        String movieUrl = PRE + FAN + number + HTML;
        String movieHtml = HttpUtils.doGetHtml(movieUrl);
        Document doc = Jsoup.parse(movieHtml);
        Elements movieDetailList = doc.select("div[class=text-box loadimg]").select("p");
        Map<String, String> map = new HashMap<>();
        for (Element element : movieDetailList) {
            map.put(element.select("strong").text(), element.text());
        }
        String photoUrl = movieDetailList.select("img").attr("src");
        Movie movie = new Movie();
        movie.setMoviename(map.get("作品名：").substring(4));
        movie.setNumber(map.get("番号：").substring(3));
        movie.setName(map.get("主演：").substring(3));
        movie.setLabel(map.get("类别标签：").substring(5));
        movie.setPhoto(HttpUtils.doGetImage(photoUrl, number,movie.getName()));
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
        this.saveMovie(movie);
        return movie;
    }

}
