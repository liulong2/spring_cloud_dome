# spring_cloud_dome
第一部分: eureka学习
### 服务注册中心配置:

引入配置

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-eureka-server -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

配置yml:

```yaml
eureka:
  instance:
    hostname: localhost # eureka服务端实例名称
  client:
#    false表示不向注册中心注册自己
    register-with-eureka: false
#    false 表示自己端就是祖册中心,我的职责就是维护服务实例,并不需要哦去检索服务
    fetch-registry: false
    service-url:
#      设置Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZne: http://${eureka.instance.hostname}:${server.port}/eureka/
```

主启动类添加注解

```java
@EnableEurekaServer
```

### eureka client端

引入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

yml配置

```yaml
eureka:
  client:
#    表示是否将自己注册进EurekaServer中 默认为true
    register-with-eureka: true
#    是否从EurekaServer中抓取已有的注册信息,默认为true 单点无所谓,集群不许设置为true才能配合ribbon使用负载均衡
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:7001/eureka
```

注解

```java
@EnableEurekaClient
```

### 集群部署

yml配置

```yaml
server:
  port: 7001
eureka:
  instance:
    hostname: eureka7001.com # eureka服务端实例名称
  client:
    #    false表示不向注册中心注册自己
    register-with-eureka: false
    #    false 表示自己端就是祖册中心,我的职责就是维护服务实例,并不需要哦去检索服务
    fetch-registry: false
    service-url:
      #      设置Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://eureka7002.com:7002/eureka/
```

```yaml
server:
  port: 7002
eureka:
  instance:
    hostname: eureka7001.com # eureka服务端实例名称
  client:
    #    false表示不向注册中心注册自己
    register-with-eureka: false
    #    false 表示自己端就是祖册中心,我的职责就是维护服务实例,并不需要哦去检索服务
    fetch-registry: false
    service-url:
      # 设置Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址  此处如果死多个地址,用逗号隔开,集群是填其他人的地址
      defaultZone: http://eureka7001.com:7001/eureka/
```

eureka client端修改yml配置

```yaml
defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
```



服务调用方,需要将地址改为微服务名称

```java
public static final String PAYMENT_URL = "http://CLOUD-PAYMEN-SERVICE";
```

开启eureka的负载均衡功能

RestTemplate可以调用其他包的服务

LoadBalanced开启负载均衡

```java
@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
```



主机名称的修改

```yaml
instance:
  instance-id: payment8002
  #    访问路径可以显示IP
  prefer-ip-address: true
```



查看自己主机上的业务

```java
@Resource
private DiscoveryClient discoveryClient;
```

查看个种信息

```java
@GetMapping("/discovery")
public Object discovery() {
    List<String> services = discoveryClient.getServices();
    services.forEach(string ->{
        log.info(MessageFormat.format("######element: {0}",string));
    });
    //通过微服务名称获得微服务全部实例
    List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMEN-SERVICE");
    instances.forEach(serviceInstance -> {
        log.info(serviceInstance.getInstanceId() + "\t" 
                + serviceInstance.getHost() + "\t" + serviceInstance.getPort() + "\t" + serviceInstance.getUri());
    });
    return this.discoveryClient;
}
```

添加注解

```java
@EnableDiscoveryClient
```

自我保护:服务端UI界面的红色字体,是有个服务可能出现问题,但是服务端并不会立即删除,而是保护90秒后再删除

禁用自我保护:

服务端 server

```yaml
  server:
#    关闭自我保护机制,保证不可用服务被即时删除
    enable-self-preservation: false
#    相应时长
    eviction-interval-timer-in-ms: 2000
```

客户端 client端

```yaml
  instance:
    instance-id: payment8001
      #    访问路径可以显示IP
    prefer-ip-address: true
#    客户端向服务端发送心跳的时间间隔 单位为秒(默认30秒)
    lease-renewal-interval-in-seconds: 1
#    服务端在收到最后一次心跳后等待时间上线,单位为秒(默认90秒),超时将剔除服务
    lease-expiration-duration-in-seconds: 2
```
20201007学习
## zookeeper

依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
</dependency>
```

yml配置 消费者和提供者一样的配置,多集群的时候connect-string配置多个,用逗号分隔

```yaml
server:
  port: 8004
spring:
  application:
    name: cloud-provider-payment
  cloud:
    zookeeper:
      connect-string: 59.110.64.192:2181
```

启动类注解

```java
EnableDiscoveryClient
```

解决jar包冲突

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-zookeeper-discovery -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
    <!--排除zk3.5.3-->
    <exclusions>
        <exclusion>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!--添加zk 3.4,9版本-->
<!-- https://mvnrepository.com/artifact/org.apache.zookeeper/zookeeper -->
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.9</version>
</dependency>
```

注册进的是临时节点

调用

```java
public static final String INVOKE_URL = "http://cloud-provider-payment";
```

## consul

依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
```

yml配置 服务提供者和服务消费者一样配置(name不同)

```yaml
server:
  port: 8006
spring:
  application:
    name: consul-provider-payment
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}
```

主启动类注解

```java
@EnableDiscoveryClient
```

## Ribbon

是一套客户端:用在用户界面的module,负载均衡工具,本地负载均衡

依赖,eureka中携带ribbon

```xml
<!--eureka client-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

修改负载均衡方式

配置不能放在ComponentScan能扫描到的包下面

```java
@Configuration
public class MySelfRule {
    
    @Bean
    public IRule myRule() {
        return new RandomRule(); //定义为随机
    }
}
```

主启动类配置 name中大小写必须一致 (可能小写可以,也可能大写可以,未看源码,不知道原因)

```java
@RibbonClient(name = "CLOUD-PAYMEN-SERVICE",configuration = MySelfRule.class)
```

原子类(需要研究一下)

自旋锁()

OpenFeign使用

在接口上加上注解就可以使用

## feigen是用在消费端

依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

注解

```java
@EnableFeignClients
```

使用,GET方法必须加上RequestParam不然会自动给你转成post请求(都是泪)

```java
@Component
@FeignClient(value = "CLOUD-PAYMEN-SERVICE")
public interface PaymentFeignService {

    @GetMapping("/payment/inquiry")
    public CommonResult getEntity(@RequestParam("id") Long id);
}

```

自带负载均衡功能

超时设置: 在客户端设置

默认1秒

yml配置

```yaml
#设置feign客户端查实时间(默认支持ribbon)
ribbon:
  #  指的是建立连接后从服务器读取到可用资源所用的时间
  ReadTimeout:  5000
  #  指的是建立连接所用的时间,适用于网络状况正产的情况下,两端连接所用的时间
  ConnectTimeout: 5000
```

日志配置

在客户端配置

config配置

```java
@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
```

yml配置

```yaml
logging:
  level:
    com.xybbz.service.PaymentFeignService: debug
```

## Hystrix

服务降级,服务熔断,接近实时的监控,有兜底解决方案,开关装置

依赖

```xml
<!--新增hystrix-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

yml配置和eureka一致即可

暂停线程

```java
TimeUnit.SECONDS.sleep(3);
```

jmeter压力测试软件

一般都是用在消费端

依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

#### 服务降级

8001上service中方法添加注解

```java
//添加熔断注解,fallbackMethod 表示兜底方法
@HystrixCommand(fallbackMethod = "payMentInfo_TimeoutHandler", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
})
```

兜底方法

```java
public String payMentInfo_TimeoutHandler(Integer id) {
    return "线程池: " + Thread.currentThread().getName() + " payMentInfo_TimeoutHandler" + id + "你超时死了";
}
```

主启动类添加

```java
@EnableCircuitBreaker
```

客户端80

配置yml 这样用是因为使用feign集成hystrix 添加这条配置会出现永远走兜底

```yaml
feign:
  hystrix:
    enabled: true
```

设置feigen的超时时间后需要设置hystrix超时时间

```java
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 10000
```



配置feign实现类也可以默认获得服务兜底的形式

实现业务与逻辑解耦

```java
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT",fallback = PaymentFallbackService.class)
```

实现类

```java
@Component
public class PaymentFallbackService implements PaymentHystrixService {
    @Override
    public String payMentInfo_OK(Integer id) {
        return "服务失败了,你个大傻子:payMentInfo_OK ";
    }

    @Override
    public String payMentInfo_timOut(Integer id) {
        return "服务失败了,你个大傻子: payMentInfo_timOut";
    }
}
```

主启动类

```java
@EnableHystrix
```

配置全局的fallback兜底,加在类上

```java
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
```

方法上必须加

```java
@HystrixCommand
```

#### 服务熔断

应对雪崩效应的一种微服务保护机制

```java
@HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
        @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),//是否开启断路器
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"), //请求次数
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//时间窗口期重试睡眠多久
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")//失败率打到多少后跳闸
})
```

默认配置在HystrixCommandProperties文件中

##### 图形化界面

依赖

```xml
<!--新增hystrix dashboard-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>
```

主启动类注解

```java
@EnableHystrixDashboard
```

所有要用的微服务包里都需要依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Gateway

反向代理,鉴权,流量控制,熔断,日志监控

路由,断言,过滤

依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```



第一种配置

yml配置

```yaml
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      routes:
        - id: payment_routh #路由的ID，没有固定规则但要求唯一，建议配合服务名
          uri: http://localhost:8001   #匹配后提供服务的路由地址
          predicates:
            - Path=/payment/get/**   #断言,路径相匹配的进行路由

        - id: payment_routh2
          uri: http://localhost:8001
          predicates:
            - Path=/payment/lb/**   #断言,路径相匹配的进行路由
```

第二种配置

```java
@Configuration
public class GateWayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route_xybbz",r -> r.path("/guonei").uri("http://news.baidu.com/guonei"));
        return routes.build();
    }
}
```

gateway和springmvc会发生冲突
