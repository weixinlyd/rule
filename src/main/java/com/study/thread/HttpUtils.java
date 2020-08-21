package com.study.thread;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.omg.SendingContext.RunTime;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

/**
 * 多线程缩短响应时间
 * 但是，这个例子并没有生效，应该是HttpClient本身就对响应做了多线程处理
 */
@Slf4j
public class HttpUtils {


    public static String client(String url) throws IOException {
        String requestParams = "";
        String resp ="";
        JSONObject jb = new JSONObject();

        try{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(300 * 1000)
                .setConnectTimeout(300 * 1000)
                .build();
        HttpPost post = new HttpPost(url);
        post.setConfig(requestConfig);
        post.setHeader("Content-Type","application/json;charset=utf-8");
        StringEntity postingString = new StringEntity(requestParams,
                "utf-8");
        post.setEntity(postingString);
        HttpResponse response = httpClient.execute(post);
        String content = EntityUtils.toString(response.getEntity());
        System.out.println(content);
        return content;
    } catch (SocketTimeoutException e) {
        log.error("调用Dat+"
                + ".aService接口超时,超时时间:" + 300
                + "秒,url:" + url + ",参数：" + requestParams, e);
        jb.put("error","接口超时"+e.getMessage());
        return jb.toString();
    } catch (Exception e) {
        log.error("调用DataService接口失败,url:" + url + ",参数：" + requestParams,
                e);
        jb.put("error","调用DataService接口失败,url:"+url);
        return jb.toString();
    }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String[] url = {"http://www.baidu.com","http://www.2345.com"};
        try {
            ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            long time1 = System.currentTimeMillis();
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<百度响应");
            String response = (String) client(url[0]);
            System.out.println(System.currentTimeMillis()-time1+"ms");
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<2345响应");
            String response1 =  client(url[1]);
            System.out.println(System.currentTimeMillis()-time1+"ms");
            Future<String> future1 = es.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return response;
                }
            });

            Future<String> future2 = es.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return response1;
                }
            });
            future1.get();
            future2.get();
            System.out.println(System.currentTimeMillis()-time1+"ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
