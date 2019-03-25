package com.hug.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * https://redis.io/topics/data-types
 * The max number of members in a set is 232 - 1 (4294967295, more than 4 billion of members per set)
 *
 * 以"跳一跳"小程序游戏为例
 *
 * 某个时间段
 * 1、显示全服最高记录前100
 * 2、个人最高分
 * 3、好友排行榜
 *
 * 展示给用户看的信息：排行 头像 昵称 分数 时间
 */
@Service
public class RedisZSetService {
    /**
     * redis的key和string类型value限制均为512MB。
     * zset:
     */

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ObjectMapper objectMapper;

    private static long TOP_NUM = 10L;
    private static String MID_AUTUMN = "TOP_LIST";      // TOP 10
    private static String MID_AUTUMN_USER = "TOP_USER_";// 个人最好记录

    MidAutumnView convStr2MidAutumnView(String jsonStr, int i) {
        MidAutumnView view = null;
        try {
            view = objectMapper.readValue(jsonStr, MidAutumnView.class);
            view.setRank(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    // 指定成员的 key = xxx + userId
    String convView2ItemStr(MidAutumnView midAutumnView) {
        RedisRankItem redisRankItem = new RedisRankItem();

        BeanUtils.copyProperties(midAutumnView, redisRankItem);

        String str = JSONObject.toJSONString(redisRankItem);

        return str;
    }

    public List<MidAutumnView> getRangeCondition(String userId, int bef, int aft) {
        Set<String> midAutumnStrs = stringRedisTemplate.opsForZSet().range(MID_AUTUMN, bef, aft);
        List<MidAutumnView> midAutumnViews = Lists.newArrayList();
        Iterator<String> iterator = midAutumnStrs.iterator();
        int i = 1;
        while (iterator.hasNext()) {
            midAutumnViews.add(convStr2MidAutumnView(iterator.next(), i));
            i++;
        }

        long total = stringRedisTemplate.opsForZSet().size(MID_AUTUMN); // 返回的记录总条数.
        int pageCount = (int) ((total-1 )/ 5) + 1;  // 返回的总页数.
        // pageCount = (int) ((total % 5 == 0) ? total / 5 : total / 5 + 1);
        return midAutumnViews;
    }

    // 获取top50
    public List<MidAutumnView> getRangeTop(String userId) {
        // top
        Set<String> midAutumnStrs = stringRedisTemplate.opsForZSet().range(MID_AUTUMN, 0, TOP_NUM);
        List<MidAutumnView> midAutumnViews = Lists.newArrayList();
        Iterator<String> iterator = midAutumnStrs.iterator();
        int i = 1;
        while (iterator.hasNext()) {
            midAutumnViews.add(convStr2MidAutumnView(iterator.next(), i));
            i++;
        }
        // 判断是否在末尾追加自己
        String midAutumnStr = stringRedisTemplate.opsForValue().get(MID_AUTUMN_USER + userId);
        if (StringUtils.isEmpty(midAutumnStr)) {
            return midAutumnViews;
        }
        MidAutumnView midAutumnView = JSONObject.parseObject(midAutumnStr, MidAutumnView.class);
        // 返回有序集中指定成员的排名，其中有序集成员按分数值递增(从小到大)顺序排列
        Long rank = stringRedisTemplate.opsForZSet().rank(MID_AUTUMN, convView2ItemStr(midAutumnView));
        if (rank != null && rank > TOP_NUM) {
            midAutumnViews.add(midAutumnView);
        }

        Long reverseRank = stringRedisTemplate.opsForZSet().reverseRank(MID_AUTUMN, convView2ItemStr(midAutumnView));
        System.out.println("rank=" + rank + ", reverseRank" + reverseRank);
        return midAutumnViews;
    }

    /**
     * 提交分数
     *
     * @param redisRankItem
     */
    public MidAutumnView putScore(RedisRankItem redisRankItem) {
        String midAutumnStr = stringRedisTemplate.opsForValue().get(MID_AUTUMN_USER + redisRankItem.getUserId());
        // 首次提交分数
        if (StringUtils.isEmpty(midAutumnStr)) {
            // 新增一个有序集合，存在的话为false，不存在的话为true
            boolean addFlag = stringRedisTemplate.opsForZSet().add(MID_AUTUMN, JSONObject.toJSONString(redisRankItem), redisRankItem.buildScore(redisRankItem.getScore()));
            stringRedisTemplate.expire(MID_AUTUMN,60 * 24 , TimeUnit.MINUTES);//设置过期时间
            // 返回有序集中指定成员的排名
            Long rank = stringRedisTemplate.opsForZSet().rank(MID_AUTUMN, JSONObject.toJSONString(redisRankItem));
            /*MidAutumnView midAutumnView1 = MidAutumnView.builder().userId(redisRankItem.getUserId())
                    .name(redisRankItem.getName()).portraitUrl(redisRankItem.getPortraitUrl())
                    .score(redisRankItem.getScore()).createTime(redisRankItem.getCreateTime())
                    .rank(rank.intValue()).maxScore(redisRankItem.getScore()).build();*/
            MidAutumnView midAutumnView = MidAutumnView.builder();
            midAutumnView.setRank(rank.intValue());
            midAutumnView.setMaxScore(redisRankItem.getScore());
            BeanUtils.copyProperties(redisRankItem, midAutumnView);

            // 新增用户单独分数记录
            stringRedisTemplate.opsForValue().set(MID_AUTUMN_USER + redisRankItem.getUserId(), JSONObject.toJSONString(midAutumnView));
            return midAutumnView;
        } else {
            // 二次提交分数
            MidAutumnView midAutumnView = JSONObject.parseObject(midAutumnStr, MidAutumnView.class);
            // midAutumnView.setScore(redisRankItem.getScore());
            // midAutumnView.setCreateTime(redisRankItem.getCreateTime());
            // 更新最高分
            if (redisRankItem.getScore() > midAutumnView.getMaxScore()) {
                // 从有序集合中移除一个或者多个元素
                Long removeFlag = stringRedisTemplate.opsForZSet().remove(MID_AUTUMN, convView2ItemStr(midAutumnView));
                // // 新增一个有序集合，存在的话为false，不存在的话为true
                boolean addFlag = stringRedisTemplate.opsForZSet().add(MID_AUTUMN, JSONObject.toJSONString(redisRankItem), redisRankItem.buildScore(redisRankItem.getScore()));
                Long rank = stringRedisTemplate.opsForZSet().rank(MID_AUTUMN, JSONObject.toJSONString(redisRankItem));

                // 新增(更新)用户单独分数记录
                midAutumnView.setMaxScore(redisRankItem.getScore());
                midAutumnView.setRank(rank.intValue());
                stringRedisTemplate.opsForValue().set(MID_AUTUMN_USER + redisRankItem.getUserId(), JSONObject.toJSONString(midAutumnView));
            }
            return midAutumnView;
        }
    }
}
