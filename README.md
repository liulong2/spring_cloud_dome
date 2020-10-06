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

