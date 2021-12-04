package com.atb.crawler.service;

import com.atb.crawler.dao.ActressDao;
import com.atb.crawler.dao.LastestDateDao;
import com.atb.crawler.entity.Actress;
import com.atb.crawler.entity.LastestDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 呆呆
 * @Datetime 2021/11/27 13:21
 */
@Service
public class LastestDateService {

    @Autowired
    LastestDateDao lastestDateDao;

    public LastestDate findByName(String name){
        return lastestDateDao.findByName(name);
    }
}
