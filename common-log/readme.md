####commons-log-delivery.jar 版本记录
接入步骤

```pom.xml
<dependency>
   <groupId>com.freemud</groupId>
   <artifactId>commons-log-delivery</artifactId>
   <version>1.3.4.RELEASE</version>
</dependency>
```

logback.xml
```
// 新增
<springProperty scope="context" name="project_name" source="spring.application.name" defaultValue="./" />

// old
<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{5} - %msg%n</pattern>
// new
<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{5} - [${project_name:- }, tno-%X{tno}] - %msg%n</pattern>
```

Controller 入口

```
Controller方法添加注解：@ApiAnnotaion （commons-log-delivery）
```


RestTemplate

在restTemplate中添加DefaultRestInterceptor
```
@Bean
public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    // ...
    RestTemplate restTemplate = new RestTemplate();

    // add restTemplate header for log:mdc:tno
    List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
    if (CollectionUtils.isEmpty(interceptors)) {
        interceptors = Lists.newArrayList(new DefaultRestInterceptor());
    } else {
        interceptors.add(new DefaultRestInterceptor());
    }
    restTemplate.setInterceptors(interceptors);

    return restTemplate;
}
```

Feign
1. 如果服务中已经实现 feign.RequestInterceptor, 在header中添加 key=x-transaction-id
```
public void apply(RequestTemplate t) {
    if (CollectionUtils.isEmpty((Collection)t.headers().get("x-transaction-id"))) {
        t.header("x-transaction-id", new String[]{MDCUtil.createTno()});
    }
    log.debug("x-tid:{}", requestTemplate.headers().get("x-transaction-id"));
}
```

2.服务中没实现feign.RequestInterceptor 可引入DefaultFeignInterceptor
```
@ImportAutoConfiguration(classes = {DefaultFeignInterceptor.class})
```

Rabbitmq

producer
生产消息是在header中埋点 key = "x-transaction-id"
```
@Bean
public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(new Jackson2JsonMessageConverter());
    template.afterPropertiesSet();
    template.setMandatory(true);
    // 埋点
    template.setBeforePublishPostProcessors(new MdcBefMessagePostProcessor());
    return template;
}
```

consumer
1. spring-rabbit 1.x版本
1.1 注解方式 @RabbitListener
重写SimpleRabbitListenerContainerFactory.messageConverter
```
SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
factory.setConnectionFactory(connectionFactory);
//      
factory.setMessageConverter(new MdcJackson2JsonMessageConverter());
```

1.2 自定义方式 RabbitListenerEndpointRegistry.registerListenerContainer
实现MessageListenerAdapter
```
//OrderConsumerConfig
SimpleRabbitListenerEndpoint endpoint= new SimpleRabbitListenerEndpoint();
endpoint.setMessageListener(new MdcMessageListenerAdapter(orderConsumer));
```

```
public class MdcMessageListenerAdapter extends MessageListenerAdapter {

    private MessagePostProcessor messagePostProcessor;

    public MdcMessageListenerAdapter(Object delegate) {
        super(delegate);
        if (null == messagePostProcessor) {
            messagePostProcessor = new MdcAftReceivePostProcessor();
        }
    }

    public MdcMessageListenerAdapter(Object delegate, MessagePostProcessor messagePostProcessor) {
        super(delegate);
        this.messagePostProcessor = messagePostProcessor;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        messagePostProcessor.postProcessMessage(message);
        super.onMessage(message, channel);
    }
}
```

2. spring-rabbit 2.x版本及以上
setAfterReceivePostProcessors：MdcAftReceivePostProcessor

```
SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
// 2.x版本支持
factory.setAfterReceivePostProcessors(new MdcAftReceivePostProcessor()));
```

