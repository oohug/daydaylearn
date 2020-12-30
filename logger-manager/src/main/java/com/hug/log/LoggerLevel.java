package com.hug.log;

public class LoggerLevel {

    /**
     * 对应日志类名称
     */
    private String name;

    /**
     * 日志等级
     * allowableValues = "ERROR,WARN,INFO,DEBUG,TRACE"
     */
    private String level;

    public LoggerLevel() {
    }

    public LoggerLevel(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public LoggerLevel(Class<?> className, String level) {
        this.name = className.getName();
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

}
