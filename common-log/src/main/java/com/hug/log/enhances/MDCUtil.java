package com.hug.log.enhances;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 *
 */
public class MDCUtil {

    /**
     * @return
     */
    public static String createTno() {
        return !StringUtils.isEmpty(getTno()) ? getTno() : UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void putTno(String tnoVal) {
        put(LogCont.X_TNO, tnoVal);
    }

    public static void removeTno() {
        if (!StringUtils.isEmpty(MDC.get(LogCont.X_TNO))) {
            remove(LogCont.X_TNO);
        }
    }

    public static String getTno() {
        return get(LogCont.X_TNO);
    }

    public static String get(String key) {
        return MDC.get(key);
    }

    public static void put(String key, String val) {
        MDC.put(key, val);
    }

    public static void remove(String key) {
        if (!StringUtils.isEmpty(MDC.get(key))) {
            MDC.remove(key);
        }
    }
}
