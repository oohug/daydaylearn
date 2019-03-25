package com.hug.service;

import lombok.Data;

@Data
public class MidAutumnView {

    private String userId;
    private String name;
    private String portraitUrl; // 比如:头像
    private double score;
    private String createTime;
    private Integer rank;
    private double maxScore;

    private static MidAutumnView midAutumnView;

    private static final String CUSTOMERTYPE_02 = "02";

    private MidAutumnView() {
    }

    public static MidAutumnView builder() {

        if (null == midAutumnView) {
            synchronized (MidAutumnView.class) {
                if (null == midAutumnView) {
                    midAutumnView = new MidAutumnView();
                }
            }
        }
        return midAutumnView;
    }
}
