spring 运行周期

实例化bean对象(通过构造方法或者工厂方法)
设置对象属性(setter等)（依赖注入）
如果Bean实现了BeanNameAware接口，工厂调用Bean的setBeanName()方法传递Bean的ID。（和下面的一条均属于检查Aware接口）
如果Bean实现了BeanFactoryAware接口，工厂调用setBeanFactory()方法传入工厂自身
将Bean实例传递给Bean的前置处理器的postProcessBeforeInitialization(Object bean, String beanname)方法
调用Bean的初始化方法
将Bean实例传递给Bean的后置处理器的postProcessAfterInitialization(Object bean, String beanname)方法
使用Bean
容器关闭之前，调用Bean的销毁方法

AOP用途
AOP用来封装横切关注点，具体可以在下面的场景中使用:
log日志
Authentication 权限
Caching 缓存
Context passing 内容传递
Error handling 错误处理
Lazy loading　懒加载
Debugging　　调试
logging, tracing, profiling and monitoring　记录跟踪　优化　校准
Performance optimization　性能优化
Persistence　　持久化
Resource pooling　资源池
Synchronization　同步
Transactions 事务


如何使用Spring AOP
可以通过配置文件或者编程的方式来使用Spring AOP。
配置可以通过xml文件来进行，大概有四种方式：
1.        配置ProxyFactoryBean，显式地设置advisors, advice, target等
2.        配置AutoProxyCreator，这种方式下，还是如以前一样使用定义的bean，但是从容器中获得的其实已经是代理对象
3.        通过<aop:config>来配置
4.        通过<aop: aspectj-autoproxy>来配置，使用AspectJ的注解来标识通知及切入点

Spring AOP代理对象的生成
Spring提供了两种方式来生成代理对象: JDKProxy和Cglib,
具体使用哪种方式生成由AopProxyFactory根据AdvisedSupport对象的配置来决定。
默认的策略是如果目标类是接口，则使用JDK动态代理技术，否则使用Cglib来生成代理。
下面我们来研究一下Spring如何使用JDK来生成代理对象，
具体的生成代码放在JdkDynamicAopProxy这个类中，直接上相关代码：
/**
    * <ol>
    * <li>获取代理类要实现的接口,除了Advised对象中配置的,还会加上SpringProxy, Advised(opaque=false)
    * <li>检查上面得到的接口中有没有定义 equals或者hashcode的接口
    * <li>调用Proxy.newProxyInstance创建代理对象
    * </ol>
    */
   public Object getProxy(ClassLoader classLoader) {
       if (logger.isDebugEnabled()) {
           logger.debug("Creating JDK dynamic proxy: target source is " +this.advised.getTargetSource());
       }
       Class[] proxiedInterfaces =AopProxyUtils.completeProxiedInterfaces(this.advised);
       findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
       return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
}
SPRING AOP原理:代理模式,（动态代理和静态代理）
动态代理:通过java反射生成对象,在其他类调用

AOP相关概念
方面（Aspect）：一个关注点的模块化，这个关注点实现可能另外横切多个对象。事务管理是J2EE应用中一个很好的横切关注点例子。方面用Spring的 Advisor或拦截器实现。
连接点（Joinpoint）: 程序执行过程中明确的点，如方法的调用或特定的异常被抛出。
通知（Advice）: 在特定的连接点，AOP框架执行的动作。各种类型的通知包括“around”、“before”和“throws”通知。通知类型将在下面讨论。许多AOP框架包括Spring都是以拦截器做通知模型，维护一个“围绕”连接点的拦截器链。Spring中定义了四个advice: BeforeAdvice, AfterAdvice, ThrowAdvice和DynamicIntroductionAdvice
切入点（Pointcut）: 指定一个通知将被引发的一系列连接点的集合。AOP框架必须允许开发者指定切入点：例如，使用正则表达式。 Spring定义了Pointcut接口，用来组合MethodMatcher和ClassFilter，可以通过名字很清楚的理解， MethodMatcher是用来检查目标类的方法是否可以被应用此通知，而ClassFilter是用来检查Pointcut是否应该应用到目标类上
引入（Introduction）: 添加方法或字段到被通知的类。 Spring允许引入新的接口到任何被通知的对象。例如，你可以使用一个引入使任何对象实现 IsModified接口，来简化缓存。Spring中要使用Introduction, 可有通过DelegatingIntroductionInterceptor来实现通知，通过DefaultIntroductionAdvisor来配置Advice和代理类要实现的接口
目标对象（Target Object）: 包含连接点的对象。也被称作被通知或被代理对象。POJO
AOP代理（AOP Proxy）: AOP框架创建的对象，包含通知。 在Spring中，AOP代理可以是JDK动态代理或者CGLIB代理。
织入（Weaving）: 组装方面来创建一个被通知对象。这可以在编译时完成（例如使用AspectJ编译器），也可以在运行时完成。Spring和其他纯Java AOP框架一样，在运行时完成织入。