package com.atb.crawler.pipeline;

import com.atb.crawler.entity.Movie;
import com.atb.crawler.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @Author 呆呆
 * @Datetime 2021/11/27 21:24
 */
@Component
public class MoviePipeline implements Pipeline {

    @Autowired
    private MovieService movieService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Movie movie = resultItems.get("movie");
        if (movie != null) {
            movieService.saveMovie(movie);
        }
    }
}
