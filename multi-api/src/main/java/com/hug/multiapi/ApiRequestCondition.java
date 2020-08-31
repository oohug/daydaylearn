package com.hug.multiapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ApiRequestCondition implements RequestCondition<ApiRequestCondition> {

    /**
     * 通过 http header 传递
     */
    private static final String API_VERSION_KEY = "api-version";
    //    private static final String API_FORMAT = "v(\\d+)";
    /**
     * 多版本规则的正则表达式；
     * 示例：支持v1.1 / V1.1 / 空1.1
     */
    private static final String API_FORMAT = "(v|V|)([0-9]\\d*\\.?\\d*)";

    private static Pattern API_PATTERN = Pattern.compile(API_FORMAT);

    private String apiVersion;
    private int missRule;

    public ApiRequestCondition(String apiVersion, int missRule) {
        this.apiVersion = apiVersion;
        this.missRule = missRule;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public int getMissRule() {
        return missRule;
    }

    @Override
    public ApiRequestCondition combine(ApiRequestCondition other) {
        // 最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiRequestCondition(other.getApiVersion(), other.getMissRule());
    }

    @Override
    public int compareTo(ApiRequestCondition other, HttpServletRequest request) {
        // 对符合请求版本的版本号进行排序 (取最近一个版本)
        log.info("{} compareTo {} = {}", other.getApiVersion(), this.apiVersion, other.getApiVersion().compareTo(this.apiVersion));
        return new BigDecimal(other.getApiVersion()).compareTo(new BigDecimal(this.apiVersion));
    }

    @Override
    public ApiRequestCondition getMatchingCondition(HttpServletRequest request) {
        // 设置默认版本号，请求版本号错误时使用最新版本号的接口
        String version = String.valueOf(Integer.MAX_VALUE);
        // 请求版本号
        String apiVersion = request.getHeader(API_VERSION_KEY);
        if (!StringUtils.isEmpty(apiVersion)) {
            Matcher m = ApiRequestCondition.API_PATTERN.matcher(apiVersion);
            if (m.find()) {
                version = m.group(2);
            }
        }
        // 返回小于等于请求版本号的版本
        boolean match = false;
        if (missRule > 0) {
            match = version.compareTo(this.apiVersion) >= 0;
        } else {
            match = version.equals(this.apiVersion);
        }
        if (match) {
            return this;
        }
        return null;
    }

    public static void main(String[] args) {

        System.out.println("10.1".compareTo("10.11"));
        System.out.println("10.1".compareTo("2.1"));
        System.out.println("2.1".compareTo("1.1"));
        System.out.println("==========");

        System.out.println(new BigDecimal("10.1").compareTo(new BigDecimal("10.11")));
        System.out.println(new BigDecimal("10.1").compareTo(new BigDecimal("2.1")));
        System.out.println(new BigDecimal("2.1").compareTo(new BigDecimal("1.1")));
        System.out.println("==========");

        String API_FORMAT = "(v|V|)([1-9]\\d*\\.?\\d*)";
        Pattern API_PATTERN = Pattern.compile(API_FORMAT);
        Matcher m = API_PATTERN.matcher("v0.1");
        if (m.find()) {
            System.out.println(m.group());
            System.out.println(m.group(1));
            System.out.println(m.group(2));
        }
    }

}