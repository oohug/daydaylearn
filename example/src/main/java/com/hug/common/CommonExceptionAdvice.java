package com.hug.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 全局异常捕获
 */
@RestControllerAdvice
@ResponseBody
public class CommonExceptionAdvice {
    private static Logger Log = LoggerFactory.getLogger(CommonExceptionAdvice.class);

    @ExceptionHandler(value = BizException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Object exception(final BizException ex, final ServletWebRequest req, final HandlerMethod handlerMethod) {
        Log.error("业务异常:{};errCode={}", req, ex.getCode(), ex);
        Map<String, Object> view = new HashMap<>(2);
        view.put("code", ex.getCode());
        if (!StringUtils.isEmpty(ex.getLocalizedMessage())) {
            view.put("message", ex.getLocalizedMessage());
        } else {
            view.put("message", ex.getMessage());
        }
        return view;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object exception(final MissingServletRequestParameterException ex, final ServletWebRequest req, final HandlerMethod handlerMethod) {
        Log.error("缺少请求参数:{}", ex.getMessage(), ex);
        Map<String, Object> view = new HashMap<>(2);
        view.put("code", CodeEnum.缺少请求参数.getCode());
        view.put("message", "缺少请求参数");
        return view;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object exception(final HttpMessageNotReadableException ex, final ServletWebRequest req, final HandlerMethod handlerMethod) {
        Log.error("参数解析失败 could_not_read_json:{}", ex.getMessage(), ex);
        Map<String, Object> view = new HashMap<>(2);
        view.put("code", CodeEnum.参数解析失败.getCode());
        view.put("message", "参数解析失败");
        return view;
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Object exception(final BindException ex, final ServletWebRequest req, final HandlerMethod handlerMethod) {
        Log.error("参数绑定失败:{}", ex.getMessage(), ex);
        BindingResult r = ex.getBindingResult();
        Map<String, Object> view = new HashMap<>(2);
        view.put("code", CodeEnum.数据绑定失败.getCode());
        view.put("message", r.getFieldErrors().get(0).getDefaultMessage()); //默认显示第一个提示信息
        return view;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Object exception(final MethodArgumentNotValidException ex, final ServletWebRequest req, final HandlerMethod handlerMethod) {
        Log.error("参数验证失败:" + ex.getMessage(), ex);
        BindingResult r = ex.getBindingResult();
        Map<String, Object> view = new HashMap<>(2);
        view.put("code", CodeEnum.参数验证失败);
        view.put("message", r.getFieldErrors().get(0).getDefaultMessage()); //默认显示第一个提示信息
        return view;
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public Object exception(final HttpRequestMethodNotSupportedException ex, final ServletWebRequest req, final HandlerMethod handlerMethod) {
        Log.error("不支持当前请求方法:" + ex.getMessage(), ex);
        Map<String, Object> view = new HashMap<>(2);
        view.put("code", CodeEnum.不支持当前请求方法);
        if (!Objects.isNull(ex.getMessage()) && !"".equals(ex.getMessage())) {
            view.put("message", ex.getMessage());//默认显示第一个提示信息
            return view;
        }
        view.put("message", "请求方法不支持");//默认显示第一个提示信息
        return view;
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Object exception(final HttpMediaTypeNotSupportedException ex, final ServletWebRequest req, final HandlerMethod handlerMethod) {
        Log.error("不支持当前媒体类型:" + ex.getMessage(), ex);
        Map<String, Object> view = new HashMap<>(2);
        view.put("code", CodeEnum.不支持当前请求方法);
        if (!Objects.isNull(ex.getMessage()) && !"".equals(ex.getMessage())) {
            view.put("message", ex.getMessage());
            return view;
        }
        view.put("message", "不支持当前媒体类型");
        return view;
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleRunTimeExceptions(final IllegalArgumentException ex, final ServletWebRequest req, final HandlerMethod handlerMethod) {
        Log.error("系统异常,参数不合法:" + req, ex);
        Map<String, Object> view = new HashMap<>(2);
        view.put("code", ApiResult.CODE_FAILED);
        view.put("message", "系统繁忙,请联系管理员!");
        return view;
    }

    /**
     * 操作数据库出现异常:名称重复，外键关联
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleException(DataIntegrityViolationException ex, final ServletWebRequest req, final HandlerMethod handlerMethod) {
        Log.error("操作数据库出现异常:" + req, ex);
        Map<String, Object> view = new HashMap<>(2);
        view.put("code", ApiResult.CODE_FAILED);
        view.put("message", "系统异常!");
        return view;
    }

    /**
     * 全局处理Exception 错误的情况下返回500
     *
     * @param ex
     * @param req
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleOtherExceptions(final Exception ex, final ServletWebRequest req) {
        Log.error("系统异常:" + req, ex);
        Map<String, Object> view = new HashMap<>(2);
        view.put("code", CodeEnum.系统异常.getCode());
        view.put("message", "系统繁忙,请稍后再试!");
        return view;
    }

}
