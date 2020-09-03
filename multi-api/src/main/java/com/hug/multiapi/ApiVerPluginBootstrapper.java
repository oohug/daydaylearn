package com.hug.multiapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ApiVerPluginBootstrapper implements SmartLifecycle {
    private static final Logger log = LoggerFactory.getLogger(ApiVerPluginBootstrapper.class);

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private TreeMap<String, Object> multiVApiMapped = new TreeMap<>();

    @Autowired
    private RequestMappingHandlerMapping handlerMethodMapping;

    @Override
    public void start() {
        if (initialized.compareAndSet(false, true)) {
            log.info("multi v-api refreshed");
            long startTime = System.currentTimeMillis();
            Map<RequestMappingInfo, HandlerMethod> methodMap = handlerMethodMapping.getHandlerMethods();
            for (RequestMappingInfo key : methodMap.keySet()) {
                HandlerMethod handlerMethod = methodMap.get(key);
                ApiVersion annotation = handlerMethod.getMethodAnnotation(ApiVersion.class);
                if (null != annotation) {
                    multiVApiMapped.put(key.getPatternsCondition().toString() + ":v" + annotation.value(), handlerMethod);
//RequestMappingHandlerMapping -
// Mapped "{[/order/receiving],methods=[GET],produces=[application/json]}" onto public com.x.common.BaseResponse<com.x.vo.response.OrderOperationResponse> com.x.controller.XController.posReceivingOrder(java.lang.String)
                }
            }

            Iterator it = multiVApiMapped.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                log.info("multi-v-api-mapped {} onto {}", entry.getKey(), entry.getValue());
            }

            log.info("multi v-api refreshed end (cost {}ms)", System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public void stop() {
        initialized.getAndSet(false);
    }

    @Override
    public boolean isRunning() {
        return initialized.get();
    }

    /**
     * 返回true时start方法会被自动执行，返回false则不会
     *
     * @return
     */
    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    public TreeMap<String, Object> getMultiVApiMapped() {
        return multiVApiMapped;
    }
}
