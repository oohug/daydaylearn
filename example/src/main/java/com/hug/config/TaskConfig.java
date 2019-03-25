package com.hug.config;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
@DisconfFile(filename = "task.properties")
@DisconfUpdateService(classes = {TaskConfig.class})
public class TaskConfig implements IDisconfUpdate {
    private final static Logger LOGGER = LoggerFactory.getLogger(TaskConfig.class);

    private String taskSwitch;
    private String taskSwitchNew;
    private String taskJobCron; // quaz的cron表达式

    @DisconfFileItem(name = "task.switch", associateField = "taskSwitch")
    public String getTaskSwitch() {
        return taskSwitch;
    }

    @DisconfFileItem(name = "task.switch.new", associateField = "taskSwitchNew")
    public String getTaskSwitchNew() {
        return taskSwitchNew;
    }

    @DisconfFileItem(name = "task.job.cron", associateField = "taskJobCron")
    public String getTaskJobCron() {
        return taskJobCron;
    }

    @Override
    public void reload() throws Exception {
        LOGGER.info("disconf.reload ");
    }
}
