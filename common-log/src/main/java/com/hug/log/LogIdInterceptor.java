package com.hug.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class LogIdInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("获取tracking的值====>{}", httpServletRequest.getHeader("x-transaction-id"));
        }
        LogTreadLocal.setTrackingNo(StringUtils.isEmpty(httpServletRequest.getHeader("x-transaction-id")) ? UUID.randomUUID().toString().replaceAll("-", "") : httpServletRequest.getHeader("x-transaction-id"));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        LogTreadLocal.removeTrackingNo();
    }

}
