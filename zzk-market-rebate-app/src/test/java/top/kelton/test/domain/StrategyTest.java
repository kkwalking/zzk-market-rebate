package top.kelton.test.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.kelton.domain.strategy.service.armory.IStrategyArmory;
import top.kelton.domain.strategy.service.armory.IStrategyDispatch;

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
    @Resource
    private IStrategyDispatch strategyDispatch;

    @Test
    public void test_assembleLotteryStrategy() {
        strategyArmory.assembleLotteryStrategy(100001L);
    }

    @Test
    public void test_assembleLotteryStrategyWithRuleWeight() {
        strategyArmory.assembleLotteryStrategy(100001L);
    }
    @Test
    public void test_getRandomAwardId() {
        Map<Long, Integer> awardCountMap = new HashMap<>();
        // 抽奖100万次，计算出概率分布
        int total = 1000_000;
        for (int i = 0; i < total; i++) {
            Long randomAwardId = strategyDispatch.getRandomAwardId(100001L);
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

    /**
     * 根据策略ID+权重值，从装配的策略中随机获取奖品ID值
     */
    @Test
    public void test_getRandomAwardId_ruleWeightValue() {
        Long strategyId = 100001L;
        String ruleWeight = "6000:102,103,104,105,106,107,108,109";
        log.info("策略ID:{} - 策略权重配置:{}", strategyId, ruleWeight);
        Map<Long, Integer> awardCountMap = new HashMap<>();
        // 抽奖10万次，计算出概率分布  一万次耗时6s 10万次40s 100万次预计耗时400s
        long start = System.currentTimeMillis();
        int total = 1000000;
        for (int i = 0; i < total; i++) {
            Long randomAwardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeight);
            // log.info("本次抽奖奖品: {}", randomAwardId);
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

        log.info("本次测试耗时:{}s", (System.currentTimeMillis() - start)/1000);
    }
}
