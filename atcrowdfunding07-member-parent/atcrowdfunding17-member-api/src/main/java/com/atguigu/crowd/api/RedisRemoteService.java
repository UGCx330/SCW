package com.atguigu.crowd.api;

import com.atguigu.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

@FeignClient("zzh-crowd-redis")
public interface RedisRemoteService {
    @GetMapping("/set/redis/key/value/remote")
    public ResultEntity<String> setRedisKeyValueRemote(@RequestParam("key") String key, @RequestParam("value") String value);

    @GetMapping("/set/redis/key/value/remote/with/timeout")
    public ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key, @RequestParam("value") String value, @RequestParam("time") long time, @RequestParam("TimeUnit") TimeUnit timeUnit);

    @GetMapping("/get/redis/string/value/by/key")
    public ResultEntity<String> getRedisStringValueByKeyRemote(@RequestParam("key") String key);

    @GetMapping("/remove/redis/key/remote")
    public ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key);
}
