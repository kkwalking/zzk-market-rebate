package top.kelton.test.infrastructure.redis;

import top.kelton.infrastructure.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Resource
    private IRedisService redissonService;

    @Test
    public void test_setValue() {
        redissonService.setValue("zzk", "test123");
        log.info("设置属性值");
    }

    @Test
    public void test_getValue() {
        String zzk = redissonService.getValue("zzk");
        log.info("测试结果:{}", zzk);
    }

    @Test
    public void test_remove() {
        redissonService.remove("60711088280");
    }


    @Test
    public void test_ReentrantLock() throws Exception {
        RLock lock = redissonService.getLock("");
        try {
            // 1. 最常见的使用方法
            lock.lock();

        } finally {
            lock.unlock();
        }
    }

}
