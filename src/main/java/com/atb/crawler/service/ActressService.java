package com.atb.crawler.service;

import com.atb.crawler.dao.ActressDao;
import com.atb.crawler.entity.Actress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 呆呆
 * @Datetime 2021/11/27 13:21
 */
@Service
public class ActressService {

    @Autowired
    ActressDao actressDao;

    public List<Actress> findAllActress(){
        return actressDao.findAll();
    }
}
