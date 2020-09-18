package com.hug.rbtmqretry;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.adapter.AbstractAdaptableMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.type.MethodMetadata;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * rabbitmq consumer retry
 * 默认重试间隔1分钟
 */
@Slf4j
@Aspect
@Component
public class RetryAspect {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // checkout @retry
    @PostConstruct
    public void init() {
        Set<MethodMetadata> annotatedMethods = AnnotationUtils.getClazzFromAnnotation("com.hug", XRetry.class);
        for (MethodMetadata method : annotatedMethods) {
            Map<String, Object> annotationAttributes = method.getAnnotationAttributes(XRetry.class.getName());
            Object retryTimes = annotationAttributes.get("retryTimes");
            Integer times = Integer.valueOf(retryTimes.toString());
            Object step = annotationAttributes.get("step");
            if (step.getClass().isArray()) {
                int length = Array.getLength(step);
                if (length > 0 && times != length) {
                    throw new IllegalArgumentException("重试@Retry 注解，需要重试的次数与步长的数组长度不一致,请修改.." + JSON.toJSONString(annotatedMethods));
                }
            }
        }
    }


    @Pointcut("@annotation(com.hug.rbtmqretry.XRetry)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.debug("retry_aspect_error, {}", e);
            if (e instanceof XRetryException) {
                try {
                    Object[] args = joinPoint.getArgs();
                    Signature signature = joinPoint.getSignature();
                    MethodSignature methodSignature = (MethodSignature) signature;
                    Method targetMethod = methodSignature.getMethod();
                    XRetry annotation = targetMethod.getAnnotation(XRetry.class);
                    if (annotation != null) {
                        /**
                         * todo 区分rbtmq消费入口是通过 【@RabbitListener】 还是 【MessageListener】，两种略有差别
                         * @RabbitListener: {@link org.springframework.amqp.rabbit.annotation.RabbitListener} -> {@link org.springframework.messaging.Message}
                         * MessageListener: {@link org.springframework.amqp.core.MessageListener} -> {@link org.springframework.amqp.core.Message}
                         */
                        Class classTarget = joinPoint.getTarget().getClass();
                        if (MessageListener.class.isAssignableFrom(classTarget) || AbstractAdaptableMessageListener.class.isAssignableFrom(classTarget)) {
                            org.springframework.amqp.core.Message message = (Message) args[0];
                            JSONObject jsonObject = (JSONObject) JSONObject.parse(message.getBody());
                            String s = jsonObject.toJSONString();
                            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
                            String exchange = message.getMessageProperties().getReceivedExchange();
                            RetryMessage retryMessage = new RetryMessage();
                            retryMessage.setErrMsg(e.getMessage());
                            retryMessage.setMsgBody(s);
                            retryMessage.setRouteKey(routingKey);
                            retryMessage.setExchange(exchange);

                            Integer retryCount = message.getMessageProperties().getHeaders().get("retryCount") != null ? (Integer) message.getMessageProperties().getHeaders().get("retryCount") : 0;
                            dealErrorRetry(retryMessage, annotation, retryCount);

                        } else if (null != targetMethod.getAnnotation(RabbitListener.class)) {
                            if (args != null && args.length > 0) {
                                String s = JSON.toJSONString(args[0]);
                                org.springframework.messaging.Message message = (org.springframework.messaging.Message) args[1];
                                String routingKey = String.valueOf(message.getHeaders().get("amqp_receivedRoutingKey"));
                                String exchange = String.valueOf(message.getHeaders().get("amqp_receivedExchange"));
                                RetryMessage retryMessage = new RetryMessage();
                                retryMessage.setErrMsg(e.getMessage());
                                retryMessage.setMsgBody(s);
                                retryMessage.setRouteKey(routingKey);
                                retryMessage.setExchange(exchange);

                                Integer retryCount = message.getHeaders().get("retryCount") != null ? Integer.valueOf(message.getHeaders().get("retryCount").toString()) : 0;
                                dealErrorRetry(retryMessage, annotation, (retryCount != null ? retryCount : 0));
                            }
                        }

                    }
                } catch (Exception ex) {
                    log.warn("retryAopError {}", ex);
                }

            }
        }
        return null;
    }

    public void dealErrorRetry(RetryMessage retryMessage, XRetry annotation, int retryCount) {
        if (annotation.retryTimes() <= 0) {
            log.debug("重试失效，不做处理");
            return;
        }

        if (retryCount >= annotation.retryTimes()) {
            log.info("retried max! exchange:{}, routeKey:{}, body:{}", retryMessage.getExchange(), retryMessage.getRouteKey(), retryMessage.getMsgBody());
            // 履约消费重试失败后永久留在死信里面；采用人工从rabbitmq控制台通过[Get Message]按钮获取消息进行处理;
            rabbitTemplate.convertAndSend(RetryRbtMQConfig.hug_DELAYING_EXCHANGE, RetryRbtMQConfig.hug_DELAYING_QUEUE_FOREVER
                    , retryMessage, msg -> {
                        msg.getMessageProperties().setHeader("retryCount", retryMessage.getRetryCount());
                        return msg;
                    });
        } else {
            int ttl = annotation.step().length > retryCount ? annotation.step()[retryCount] * 1000 : 60 * 1000;
            retryMessage.setRetryMax(annotation.retryTimes()); // 重试上限
            retryMessage.setRetryCount(retryCount + 1);// 第几次重试
            log.info("dealErrorRetry ,发起第{}次重试delay({})ms，exchange:{}, routeKey:{}, body:{}", retryMessage.getRetryCount(), ttl, retryMessage.getExchange()
                    , retryMessage.getRouteKey(), retryMessage.getMsgBody());
            rabbitTemplate.convertAndSend(RetryRbtMQConfig.hug_DELAY_EXCHANGE
                    , RetryRbtMQConfig.hug_RETRY_DELAYED_QUEUE, retryMessage, msg -> {
                        msg.getMessageProperties().setDelay(ttl);
                        msg.getMessageProperties().setHeader("retryCount", retryMessage.getRetryCount());
                        return msg;
                    });
        }
    }

    public static void main(String[] args) {
        System.out.println(TimeUnit.DAYS.toMillis(365));
        System.out.println(Integer.MAX_VALUE);
        System.out.println((int) TimeUnit.DAYS.toMillis(365));
        System.out.println(TimeUnit.DAYS.toMillis(365) > Integer.MAX_VALUE);
    }
}
