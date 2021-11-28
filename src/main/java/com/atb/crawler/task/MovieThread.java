package com.atb.crawler.task;

import com.atb.crawler.entity.Actress;
import com.atb.crawler.entity.Movie;
import com.atb.crawler.service.MovieService;
import com.atb.crawler.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author 呆呆
 * @Datetime 2021/11/27 18:35
 */
public class MovieThread extends Thread {

    @Autowired
    private MovieService movieService;
    private static String PRE = "https://www.vipfanhao.com/";
    private static String FAN = "fan/";
    private static String YOU = "you/";
    private static String HTML = ".html";

    @Override
    public void run() {
        Actress actress = MovieService.TASK_LIST.remove(0);
        System.out.println("开始爬取" + actress.getName() + "的数据");
        String urlName = actress.getUrlname();
        String url = PRE + YOU + urlName + HTML;
        // 按照页面对手机的搜索结果进行遍历解析
        String html = HttpUtils.doGetHtml(url);
        List<String> numberList = movieService.getNumberList(html);
        List<Movie> movieList = movieService.getMovieList(numberList);
        System.out.println(actress.getName() + "的数据抓取完成");
        movieService.saveMovieList(movieList);
        if (MovieService.TASK_LIST.size() != 0) {
            new MovieThread().start();
        }
    }
}
