package top.kelton.test.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.kelton.domain.strategy.service.IStrategyArmory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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
        // 定义一个 Map 来存储每个奖品的中奖次数
        Map<Long, Integer> awardCountMap = new HashMap<>();
        // 抽奖一万次，计算出概率分布
        int total = 1000_000;
        for (int i = 0; i < total; i++) {
            Long randomAwardId = strategyArmory.getRandomAwardId(100001L);
            log.info("本次抽奖奖品: {}", randomAwardId);
            // 更新奖品的中奖次数
            awardCountMap.put(randomAwardId, awardCountMap.getOrDefault(randomAwardId, 0) + 1);
        }
        log.info("本次抽样次数为{}, 整体概率统计如下:", total);

        // 打印出概率分布，列出奖品id对应的中奖总次数、中奖概率
        for (Map.Entry<Long, Integer> entry : awardCountMap.entrySet()) {
            Long awardId = entry.getKey();
            int count = entry.getValue();
            double probability = (double) count / total * 100;

            log.info("\t奖品id:{}, 奖品中奖次数:{}, 奖品中奖概率:{}%", awardId, count, probability);
        }
    }
}
