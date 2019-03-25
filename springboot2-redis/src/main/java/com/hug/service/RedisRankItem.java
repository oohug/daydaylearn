package com.hug.service;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "分数排行信息", description = "分数排行信息")
public class RedisRankItem {

    private String userId;
    private String name;
    private String portraitUrl;
    private double score;
    private String createTime;

    public double buildScore(double score) {
        return Double.valueOf(score);
    }

}
