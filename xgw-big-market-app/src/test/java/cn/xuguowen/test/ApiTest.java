package cn.xuguowen.test;

import cn.xuguowen.infrastructure.persistent.redis.IRedisService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private IRedisService redisService;

    @Test
    public void test() {
        // 当你对返回的 RMap 对象执行 put 或 putAll 等修改操作时，Redisson 会在 Redis 中创建并存储这个 key 及其对应的值。即使你在获取 RMap 对象后暂时不进行任何操作，Redisson 也不会在 Redis 中创建任何不必要的空 key。
        RMap<Object, Object> map = redisService.getMap("strategy_id_100001");
        log.info("map:{}", JSON.toJSONString(map));
        map.put(1,101);
        map.put(2,101);
        map.put(3,101);
        map.put(4,102);
        map.put(5,102);
        map.put(6,103);
        map.put(7,104);
        log.info("map:{}", JSON.toJSONString(map));
        Object strategy_id_100001 = redisService.getFromMap("strategy_id_100001", 1);
        log.info("测试结果：{}",strategy_id_100001);
    }

}
