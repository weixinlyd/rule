package com.study.rule;

import com.study.config.CustomFilterInvocationSecurityMetadataSource;
import com.study.entity.User;
import com.study.service.impl.UserService;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class RuleApplicationTests {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Test
    void contextLoads() {
    }
    //测试数据加密
    @Test
    void test1() {
        User user = (User) userService.loadUserByUsername("admin");
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<查询数据库成功>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(user.getPassword());
        System.out.println("查询数据库成功,加密成功");
    }
//    @Test
//    public void test2() throws Exception {
//        UserToken userToken = new UserToken();
//        userToken.setUsername("admin");
//        userToken.setUserId(1);
//        String value = JwtUtils.generateToken(userToken,2);
//        System.out.println(value);
//        System.out.println("查询数据库成功,加密成功");
//    }

    //测试配置，通过url来获取角色

    //redis测试
    @Test
    public void test2() {
        String key = "user";
        String value = "tom";
        redisTemplate.opsForValue().set(key,value);
        System.out.println(redisTemplate);
        System.out.println("成功");
    }

    /**
     * 多线程
     * 从这个例子可以看出,线程赋值,并发情况下,结果可能不准确,count应该为10000，
     * 但是打印出来的结果不准
     * 因为count++相当于
     * count==tmp
     * tmp=conut+1
     * tmp=count
     * 最后一个tmp值不一定是9999
     *
     * synchronized锁同一对象时是有互斥行（两个线程同时执行，当第一个值行完释放锁，第二个才能执行）
     *
     *
     */
    private static int count ;
    private Object object = new Object();
    @Test
    public void test3() throws InterruptedException {
        List<Thread> list = new ArrayList<>();
        int max =10000;

        for(int i=0 ;i<max;i++
        ){
            System.out.println("主线层执行");
            Thread thread =new Thread(){
                @Override
                public void run() {
                    synchronized (object){
                        System.out.println("子线程运行");
                        count++;
                        System.out.println("子线程运行结束");
                    }
                }
            };
            list.add(thread);
            thread.start();
            System.out.println("主线程运行结束");
        }
        for(Thread t:list){
            //join方法会先执行停止当前线程,而执行执行join()方法的线程,
            //直到该线程执行完,停止的线程才会继续执行。
            t.join();
            System.out.println(count);
        }
        System.out.println(count);
    }

    /**
     * synchronized关键字,线程同步锁
     * 有三种锁方式,
     * static方法,普通方法,synchronied(this)
     * 静态块，方法块，代码块
     */
    public static synchronized void  test33(){
        /**
         * 静态块相当给类上锁
         */
        synchronized (RuleApplicationTests.class){

        }
    }

    /**
     * 普通方法
     */
    public synchronized void test44(){
        /**
         * 锁该方法
         */
        synchronized (this){

        }
    }

    /**
     * synchronized()
     */
    public void test55(){
        synchronized (this){

        }
    }
        public static void main(String[] args) {
            System.out.println("Main 线程 开始运行!");
            Thread t1 = new Thread(){
                @Override
                public void run(){
                    System.out.println("t1 开始运行!");
                    System.out.println("t1 结束运行!");
                }
            };
            t1.start();
            System.out.println("Main 线程 结束运行!");
        }

    /**
     * //join方法会先执行停止当前线程,而执行执行join()方法的线程,
     *             //直到该线程执行完,停止的线程才会继续执行。
     * @throws InterruptedException
     */
    @Test
    public void a() throws InterruptedException {
        System.out.println("Main 线程 开始运行!");
        Thread t1 = new Thread(){
            @Override
            public void run(){
                synchronized (this){
                    System.out.println("t1 开始运行!");
                    System.out.println("t1 结束运行!");
                }

            }
        };
        t1.start();
        t1.join();
        System.out.println("Main 线程 结束运行!");

        }


}
