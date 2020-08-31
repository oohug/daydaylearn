package hug.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OrderSuspendStatus {
    normal(1, "正常", true),
    user_reject(2, "未成团挂起", true),
    risk(3, "风控挂起", true),
    cancel_in_selling(4, "售中取消挂起", true),
    ;

    private int code;
    private String name;
    private Boolean isCanRefund;// 标识挂起状态时，是否能直接退款

    public static OrderSuspendStatus getByCode(Integer code) {
        if (Objects.isNull(code)) {
            return normal;
        }
        return Arrays.stream(OrderSuspendStatus.values())
                .filter(e -> e.code == code)
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
    }
}
