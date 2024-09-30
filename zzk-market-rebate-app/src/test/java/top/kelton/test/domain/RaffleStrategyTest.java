package top.kelton.test.domain;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import top.kelton.domain.strategy.model.entity.RaffleAwardEntity;
import top.kelton.domain.strategy.model.entity.RaffleFactorEntity;
import top.kelton.domain.strategy.service.armory.IStrategyArmory;
import top.kelton.domain.strategy.service.raffle.IRaffleStrategy;
import top.kelton.domain.strategy.service.rule.filter.RuleLockLogicFilter;
import top.kelton.domain.strategy.service.rule.filter.RuleWeightLogicFilter;

import javax.annotation.Resource;

/**
 * @author zzk
 * @description 抽奖策略-测试
 * @created 2024/9/27
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyTest {

    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IRaffleStrategy raffleStrategy;
    @Resource
    private RuleWeightLogicFilter ruleWeightLogicFilter;
    @Resource
    private RuleLockLogicFilter ruleLockLogicFilter;

    private Long strategyId = 100003L;

    @Before
    public void setUp() {
        strategyId = 100001L;
        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(strategyId));
        ReflectionTestUtils.setField(ruleWeightLogicFilter, "userScore", 100L);

        ReflectionTestUtils.setField(ruleLockLogicFilter, "userRaffleCount", 10L);
    }

    @Test
    public void test_raffle() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("zzk")
                .strategyId(100001L)
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.raffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_raffle_blacklist() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("user003")  // 黑名单用户 user001,user002,user003
                .strategyId(100001L)
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.raffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_raffle_center_rule_lock(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("zzk")
                .strategyId(strategyId)
                .build();

        for(int i = 0; i< 2; i++) {
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.raffle(raffleFactorEntity);

            log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
            log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
        }
    }

}
