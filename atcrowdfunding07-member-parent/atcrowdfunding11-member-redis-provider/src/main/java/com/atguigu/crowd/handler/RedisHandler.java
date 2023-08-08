package com.atguigu.crowd.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
public class RedisHandler {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/set/redis/key/value/remote")
    public ResultEntity<String> setRedisKeyValueRemote(@RequestParam("key") String key, @RequestParam("value") String value) {
        try {
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            ops.set(key, value);
            return ResultEntity.failed(CrowdConstant.MESSAGE_PHONE_NOT_EXISTS);
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

    @GetMapping("/set/redis/key/value/remote/with/timeout")
    public ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key, @RequestParam("value") String value, @RequestParam("time") long time, @RequestParam("TimeUnit") TimeUnit timeUnit) {
        // redis：TTL key 查看过期时间
        try {
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            ops.set(key, value, time, timeUnit);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @GetMapping("/get/redis/string/value/by/key")
    public ResultEntity<String> getRedisStringValueByKeyRemote(@RequestParam("key") String key) {
        try {
            if (stringRedisTemplate.hasKey(key)) {
                ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
                String value = ops.get(key);
                return ResultEntity.successWithData(value);
            }
            return ResultEntity.failed(CrowdConstant.MESSAGE_PHONE_NOT_EXISTS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @GetMapping("/remove/redis/key/remote")
    public ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key) {
        try {
            stringRedisTemplate.delete(key);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
