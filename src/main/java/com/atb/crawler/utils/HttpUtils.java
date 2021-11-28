package com.atb.crawler.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.SocketException;
import java.util.Base64;

/**
 * @Author 呆呆
 * @Datetime 2021/11/27 13:01
 */
public class HttpUtils {
    private static PoolingHttpClientConnectionManager cm;

    private HttpUtils() {
        throw new RuntimeException("静态工具类你创建个球的对象");
    }

    public static void init() {
        cm = new PoolingHttpClientConnectionManager();
        // 设置最大连接数
        cm.setMaxTotal(100);
        // 设置每个主机的最大连接数
        cm.setDefaultMaxPerRoute(10);
    }

    /**
     * 根据请求地址下载页面数据
     *
     * @param url
     * @return 页面数据
     */
    public static String doGetHtml(String url) {
        // 获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        // 创建httpGet请求对象, 设置url地址
        HttpGet httpGet = new HttpGet(url);

        // 设置请求信息
        httpGet.setConfig(getConfig());

        // 设置请求头, 伪装用户
        setHeaders(httpGet);

        CloseableHttpResponse response = null;

        try {
            // 使用HttpClient发起请求, 获取响应
            response = httpClient.execute(httpGet);

            // 解析响应, 返回结果
            if (response.getStatusLine().getStatusCode() == 200) {
                // 判断响应体Entity是否不为空, 如果不为空就可以使用EntityUtils
                if (response.getEntity() != null) {
                    String content = EntityUtils.toString(response.getEntity(), "utf8");
                    return content;
                }
            }
        } catch (SocketException se) {
            System.out.println(url + "妈蛋的,太卡了,请求失败");
        } catch
        (ConnectTimeoutException cte) {
            System.out.println(url + "妈蛋的,太卡了,请求失败");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭response
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 返回空串
        return "";
    }

    /**
     * 下载图片
     *
     * @param url
     * @return 图片名称
     */
    public static String doGetImage(String url, String number,String name) {
        // 获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        // 创建httpGet请求对象, 设置url地址
        HttpGet httpGet = new HttpGet(url);

        // 设置请求信息
        httpGet.setConfig(getConfig());

        // 设置请求头, 伪装用户
        setHeaders(httpGet);

        CloseableHttpResponse response = null;

        try {
            // 使用HttpClient发起请求, 获取响应
            response = httpClient.execute(httpGet);

            // 解析响应, 返回结果
            if (response.getStatusLine().getStatusCode() == 200) {
                // 判断响应体Entity是否不为空, 如果不为空就可以使用EntityUtils
                if (response.getEntity() != null) {
                    // 下载图片
                    // 获取图片的后缀
                    String extName = url.substring(url.lastIndexOf("."));
                    // 创建图片名, 重命名图片
                    String picName = number + extName;
                    // 下载图片
                    // 声明OutPutStream
                    File file = new File("D:\\666\\photo\\" + name);
                    if(!file .exists()  && !file .isDirectory()) file.mkdir();
                    OutputStream outputStream = new FileOutputStream(new File("D:\\666\\photo\\" + name+"\\"+ picName));
                    response.getEntity().writeTo(outputStream);
                    // 返回图片名称
                    System.out.println("下载" + name + "的图片成功");
                    return "D:/666/photo" + picName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭response
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 如果下载失败, 返回空串
        System.out.println("下载" + name + "的图片失败");
        return "";
    }

    // 设置请求信息
    private static RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(10000) // 创建连接的最长时间
                .setConnectionRequestTimeout(3000) // 获取连接的最长时间
                .setSocketTimeout(60000) // 数据传输的最长时间
                .build();
        return config;
    }

    // 设置请求头
    private static void setHeaders(HttpGet httpGet) {
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        //httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
    }
}