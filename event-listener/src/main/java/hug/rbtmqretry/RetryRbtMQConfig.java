package hug.rbtmqretry;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetryRbtMQConfig {

    // 履约消费重试失败后永久留在死信里面；采用人工从rabbitmq控制台通过[Get Message]按钮获取消息进行处理
    public static final String hug_DELAYING_EXCHANGE = "hug_delaying_exchange";
    public static final String hug_DELAYING_QUEUE_FOREVER = "forever";
    public static final String hug_DELAYING_QUEUE = "hug_delaying_queue";
    // argument 常量
    public static final String x_dead_letter_exchange = "x-dead-letter-exchange";
    public static final String x_dead_letter_routing_key = "x-dead-letter-routing-key";
    public static final String x_message_ttl = "x-message-ttl";

    // 重试队列
    public static final String hug_DELAY_EXCHANGE = "hug_delay_exchange";
    public static final String hug_RETRY_DELAYED_QUEUE = "hug_retry_delayed_queue";

    @Bean
    DirectExchange delayingExchange() {
        return new DirectExchange(hug_DELAYING_EXCHANGE);
    }

    @Bean
    Queue delayingQueueForever() {
        return QueueBuilder.durable(hug_DELAYING_QUEUE)
//                .withArgument(x_dead_letter_exchange, GlobalConstant.DELIVERY_DELAY_EXCHANGE)
//                .withArgument(x_dead_letter_routing_key, GlobalConstant.hug_RETRY_DELAYED_QUEUE)
                .withArgument(x_message_ttl, TimeUnit.DAYS.toMillis(365))
//                .withArgument(x_message_ttl, Integer.MAX_VALUE)
                .build();
    }

    @Bean
    Binding delayingSecond10Binding() {
        return BindingBuilder.bind(delayingQueueForever()).to(delayingExchange()).with(hug_DELAYING_QUEUE_FOREVER);
    }

}
