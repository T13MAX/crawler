package com.atb.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author 呆呆
 * @Datetime 2021/11/27 12:47
 */
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;//主键 id
    private String number;//番号
    private String name;//演员名字
    private String moviename;//comment'作品名
    private String label;//类别标签
    private int grade;//等级 好看在 0 1 2之间 3是凑合 4步兵好看 5步兵凑合 6是进入下载状态 7下载完了 8是刚入库 9是下载过不行删了
    private String photo;//图片路径
    private Date date;//发布日期
    private Date createdate;//本数据的创建日期


    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", moviename='" + moviename + '\'' +
                ", label='" + label + '\'' +
                ", grade=" + grade +
                ", photo='" + photo + '\'' +
                ", date=" + date +
                ", createdate=" + createdate +
                '}';
    }
}
