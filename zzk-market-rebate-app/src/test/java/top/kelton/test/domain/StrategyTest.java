package top.kelton.test.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.kelton.domain.strategy.service.IStrategyArmory;

import javax.annotation.Resource;

/**
 * @description: 策略领域-测试
 * @author: zzk
 * @create: 2024-09-25 23:03
 **/

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {
    @Resource
    private IStrategyArmory strategyArmory;

    @Test
    public void test_assembleLotteryStrategy() {
        strategyArmory.assembleLotteryStrategy(100001L);
    }
    @Test
    public void test_getRandomAwardId() {
        strategyArmory.getRandomAwardId(100001L);
    }
}
