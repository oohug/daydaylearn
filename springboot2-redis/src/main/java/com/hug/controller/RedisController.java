package com.hug.controller;

import com.hug.service.MidAutumnView;
import com.hug.service.RedisRankItem;
import com.hug.service.RedisZSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/")
@Api(value = "RedisController", tags = "RedisController", description = "RedisController", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RedisController {

    @Resource
    private RedisZSetService redisZSetService;

    @ApiOperation(value = "提交分数", notes = "提交分数更新排行榜")
    @PostMapping(value = "/putScore")
    @ResponseBody
    public MidAutumnView putScore(@RequestBody RedisRankItem rankItem) {
        return redisZSetService.putScore(rankItem);
    }

    @ApiOperation(value = "排行榜（10）", notes = "排行榜")
    @PostMapping(value = "/getRangeTop")
    @ResponseBody
    public List<MidAutumnView> getRangeTop(String userId) {
        return redisZSetService.getRangeTop(userId);
    }

    @ApiOperation(value = "排行榜分页", notes = "排行榜分页")
    @PostMapping(value = "/page")
    @ResponseBody
    public List<MidAutumnView> getPageData(String userId, int pageNo) {

        int bef = 0;
        int aft = 4;

        int pageSize = 5;

        bef = (pageNo > 0 ? (pageNo - 1) : 0) * pageSize;
        aft = bef + 4;
        return redisZSetService.getRangeCondition(userId, bef, aft);
    }
}
