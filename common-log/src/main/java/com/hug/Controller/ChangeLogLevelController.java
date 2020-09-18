package com.hug.Controller;

import java.io.Writer;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hug.log.ApiAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 动态修改log4j日志级别
 */
@Slf4j
@Controller
@RequestMapping(value = "/logger")
public class ChangeLogLevelController {
    /**
     * @param packageName 需要修改的包名
     * @param targetLevel 日志级别
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/change", method = RequestMethod.GET, produces = "application/json")
    public String change(String packageName, String targetLevel) {
        try {
            Level level = Level.toLevel(targetLevel, Level.INFO);
            Logger logger = LogManager.getLogger(packageName);
            logger.setLevel(level);
        } catch (Exception e) {
            return "failed";
        }
        return "success";
    }


    /**
     * 修改全局日志级别，但是我测试的时候只修改了第三方jar的日志级别，我的项目包的日志级别没有修改成功
     * 可获取到Logger们后循环遍历设置
     *
     * @param targetLevel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeRoot", method = RequestMethod.GET, produces = "application/json")
    public String change(String targetLevel) {
        try {
            Level level = Level.toLevel(targetLevel, Level.INFO);
            LogManager.getRootLogger().setLevel(level);
        } catch (Exception e) {
            return "failed";
        }
        return "success";
    }

    /**
     * 查看现在包的日志级别
     *
     * @return
     */
    @RequestMapping(value = "/loggers", method = RequestMethod.GET)
    public void index(HttpServletRequest request, HttpServletResponse response) {

        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<html>");
            Writer writer = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=utf-8");
            Enumeration logs = LogManager.getCurrentLoggers();
            while (logs.hasMoreElements()) {
                Logger logger = (Logger) logs.nextElement();
                sb.append("<span style='display:block;'>");
                //根据名称  显示指定包名的
                sb.append(logger.getName()).append(",").append(logger.getEffectiveLevel());
                sb.append("</span>");
            }
            sb.append("</html>");
            writer.write(sb.toString());
            writer.flush();
            if (writer != null) {
                writer.close();
            }
            log.info("loggers {}", sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

} 