package com.atb.crawler.controller;

import com.atb.crawler.entity.Actress;
import com.atb.crawler.entity.Movie;
import com.atb.crawler.service.ActressService;
import com.atb.crawler.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Author 呆呆
 * @Datetime 2021/11/27 14:03
 */
@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private ActressService actressService;

    @RequestMapping("index")
    public String index(ModelMap map) {
        List<Actress> allActress = actressService.findAllActress();
        map.put("list",allActress);
        return "index";
    }

    @RequestMapping("test")
    @ResponseBody
    public String test1() {
        Movie byNumber = movieService.findByNumber("FSDSS-320");
        System.out.println(byNumber);
        return "ok";
    }
}
