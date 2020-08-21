# 线程
启动线程方法,run方法不会创建新线程,而wait()方法会创建一个新的线程在启动run方法
该例子在遍历创建了5个新的线程并且让线程进入等待池,
验证时会发现执行run方法不会往下执行，因为没有线程,而wait()方法会继续执行，因为创建了新的线程。
notify()方法会随机唤醒等待池的一个线程,而换成notifAll()方法会将所有等待池的线程加入锁池中


多线程
线程里的变量  
存储顺序  变量->本地内存->主内存
当两个线程锁同一个对象时,线程A里的变量A与线程B里的变量A值相等.(赋值存到主内存，线程共享)

从这个例子可以看出,线程赋值,并发情况下,结果可能不准确,count应该为10000，
但是打印出来的结果不准
因为count++相当于
 count==tmp
 tmp=conut+1
 tmp=count
 最后一个tmp值不一定是9999
 synchronized
 同步锁同一对象时是有互斥行（两个线程同时执行，当第一个值行完释放锁，第二个才能执行）
 synchronized关键字
 static方法,普通方法,synchronied(this)
 静态块，方法块，代码块
 静态块相当给类上锁
 方法块相当于给该方法上锁
 代码块给任意对象上锁
 
 线程安全
 threadLocal(线程安全,)应用例子:httpRequest，threadLocal<httpRequest>,保证每个请求都有一个独立的线程，互不干扰
 栈封闭(意思是局部变量,不是全局变量,全局变量不是线程安全的)
 
 线程运行的三种方式
 实现Runnable接口
 thread(继承) thread可以重写run方法
 使用Callable和Future
 Java5之后，提供了Callable接口，看起来像是Runnable接口的增强版：该接口提供call()方法来作为线程执行体。与run()相比，call()方法更强大，该方法可以有返回值，并且可以声明抛出异常。
 Java5给出了Future接口，来获得call()方法的返回值。并提供了FutureTask实现类，该类实现了Runnale接口和Future接口---可以作为Thread类的target。
 
 
 线程的五种状态
 开始状态（new） 
 就绪状态（runnable）
 运行状态（running）
 阻塞状态（blocked）
 结束状态（dead）
 
 线程池
 executor框架
 Executor.newFixedThreadPool()       (固定线程池的大小)
 Executors.newCachedThreadPool()     (当线程满时，新建线程)
 Executors.newScheduledThreadPool()    (具有定时任务特性的线程池)
 
 
 executor
 executor 生命周期
 延迟任务与周期任务
 Callable与Future 带返回值的线程任务
 
 任务限时
    设置超时
    取消超时处理
 CompletionService   
    一组线程任务，执行完成的有限获取结果
    
  threadPoolExecutor配置
    线程创建与销毁相关  
        corePoolSize 线程池基础大小
        maximumPoolSize 线程池最大值
        keepAliveTime 当线程池大小超过基础大小，超出部分的线程的空闲时间超过keepAliveTime,将被回收
    管理队列任务
        无界队列
        有界队列
        饱和策略
            :RejectedExecutionHandler
          AbortPolicy  
          抛出RejectedExecutionException异常
          CallerRunsPolicy
          在线程池当前正在运行的Thread线程池中处理被拒绝的任务
          DiscardOldestPolicy
          线程池会放弃当代队列中最旧的未处理任务，然后将被拒绝的任务添加到等待队里中
          DiscardPolicy
          线程池将丢弃被拒绝的任务
        线程工厂    
            :ThreadFactory
        unit 超时设置的时间单位    
        workQueue : 保存runnable的队列
        handler 当workQueue溢出时执行的策略
        
 死锁
    获取不到锁,线程一直处于阻塞状态
    锁顺序不一定导致死锁
       锁内嵌顺序
    协同对象之间死锁    
       资源死锁
 尽量避免new Thread().start()开启线程      
 使用Exector框架或者线程池执行批量执行多线程任务
 线程池大小，需要考虑CPU核心数进行动态配置,以适应不同机器。
 可通过Runtime.getRuntime().availableProcessors()获取服务器CPU核心数。
 一般线程都需要有中断策略。
 在使用synchronized进行内嵌时,一定要保证顺序一致
 当线程发生活跃性问题，考虑是不是有死锁了。
 
 多线程
 Callable和runnable区别 
 Callable有返回值Future runnable
 Future<T> future = es.submit();
 future.get() 该方法为获取返回值，但是该方法在线程为执行完调用会形成阻塞状态
 ，线程执行完才能正常调用。若该线程已经执行完毕，则可以直接调用
 get(long var1, TimeUnit var3)这个方法就是设置等待时间的，拿取返回值。
 
 
 多线程submit()和execute()执行方法的区别
 使用submit方法还有一个特点就是，他的异常可以在主线程中catch到。
 而使用execute方法执行任务是捕捉不到异常的。
 
 显示锁
 Lock接口和synchronized的比较：
 1，synchronized代码更简洁
 2，Lock，效率比隐士锁更高
 3，Lock可以在获取锁可以被中断，超时获取锁，尝试获取锁
 
 构建自定义同步工具
 相当于自定义一个 同步锁
 Condition notFull = lock.newCondition();
 Condition notEmpty = lock.newCondition();
       