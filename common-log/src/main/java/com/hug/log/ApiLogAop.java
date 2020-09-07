package com.hug.log;

import com.hug.log.enhances.LogCont;
import com.hug.log.enhances.MDCUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Aspect
@Component
public class ApiLogAop implements Ordered {

    public ApiLogAop() {
        System.out.printf("{test");
    }

    @Pointcut("@annotation(com.hug.log.ApiAnnotaion)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        StringBuilder logMethod = new StringBuilder(joinPoint.getSignature().getDeclaringTypeName());
        logMethod.append(".");
        logMethod.append(joinPoint.getSignature().getName());
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("非法参数使用");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // set log:mdc:tno
        String tno = MDCUtil.getTno(); // 当前MDC:tno
        if (StringUtils.isEmpty(tno)) {
            String x_tid = request.getHeader(LogCont.X_TRANSACTION_ID); // http:header:x-t-id
            x_tid = !StringUtils.isEmpty(x_tid) ? x_tid : LogTreadLocal.getTrackingNo(); // 兼容原有的trackingNo
            tno = !StringUtils.isEmpty(x_tid) ? x_tid : UUID.randomUUID().toString().replace("-", "");
            MDCUtil.putTno(tno);
        }

        msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        ApiAnnotation apiAnnotation = currentMethod.getAnnotation(ApiAnnotation.class);
        Object object = joinPoint.proceed();

        log.info("method:{}, logTitle:{}, request:{}, cost:{}, args:{}, response:{}"
                , logMethod.toString(), apiAnnotation.logMessage(), request, System.currentTimeMillis() - startTime, getLogArgs(currentMethod, joinPoint), object);

        // remove log:mdc:tno
        MDCUtil.removeTno();
        return object;
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        Signature sig = joinPoint.getSignature();
        Method currentMethod = null;
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("非法参数使用");
        }
        msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        try {
            currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        }
        StringBuilder logMethod = new StringBuilder(joinPoint.getSignature().getDeclaringTypeName());
        logMethod.append(".");
        logMethod.append(joinPoint.getSignature().getName());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.error("method:{}, logTitle:{}, request:{}, args:{}, cause:{}"
                , logMethod.toString(), "", request, getLogArgs(currentMethod, joinPoint), e);

        // 此处remove 会导致统一异常处理中获取不到 log:mdc:tno
//        MDCUtil.removeTno();
    }

    private List getLogArgs(Method method, JoinPoint joinPoint) {
        if (method == null) {
            return null;
        }
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }
        List logArgs = new ArrayList();
        Parameter[] parameters = method.getParameters();
        for (int j = 0; j < parameters.length; j++) {
            Parameter parameter = parameters[j];
            LogParams logParams = parameter.getAnnotation(LogParams.class);
            if (logParams == null) {
                continue;
            }
            logArgs.add(args[j]);
        }
        return logArgs;
    }


    @Override
    public int getOrder() {
        return 1;
    }

}
