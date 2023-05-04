package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @version 1.0
 * @Description:
 * @author: Edison
 * @date: 2023/4/26 15:37
 */
@Component
public class RedisIdWorker {

    //开始时间戳
    private static final  long BEGIN_TIMESTAMP = 1682467200L;

    //序列号的位数
    private static final int COUNT_BITS = 32;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public long nextId(String keyPrefix) {
        //1.生成时间戳
        LocalDateTime time = LocalDateTime.now();
        long second = time.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = second - BEGIN_TIMESTAMP;

        //2.生成序列号
        //2.1获取当前日期，精确到天
        String date = time.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        //2.2自增长
        Long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        //3.拼接并返回
        return timeStamp << COUNT_BITS | count;
    }

}
