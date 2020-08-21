package hug.rbtmqretry;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RetryMessage implements Serializable {

    private String exchange;
    private String routeKey;
    private String msgQueue;
    private String msgBody; // 消息内容
    private String errMsg;
    private int retryMax;   // 最大重试次数
    private int retryCount; // 第几次重试

}

