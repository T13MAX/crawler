package com.atb.crawler.dao;

import com.atb.crawler.entity.LastestDate;
import com.atb.crawler.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author 呆呆
 * @Datetime 2021/11/27 12:51
 */

public interface LastestDateDao extends JpaRepository<LastestDate, Integer> {
    public LastestDate findByName(String name);
}
