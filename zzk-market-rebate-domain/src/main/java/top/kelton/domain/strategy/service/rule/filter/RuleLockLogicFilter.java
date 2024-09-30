package top.kelton.domain.strategy.service.rule.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.kelton.domain.strategy.model.entity.RuleActionEntity;
import top.kelton.domain.strategy.model.entity.RuleMatterEntity;
import top.kelton.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import top.kelton.domain.strategy.repository.IStrategyRepository;
import top.kelton.domain.strategy.service.annotion.LogicStrategy;
import top.kelton.domain.strategy.service.rule.factory.DefaultLogicFactory;

import javax.annotation.Resource;

/**
 * @description: 抽奖次数解锁
 * @author: zzk
 * @create: 2024-09-29 21:38
 **/
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

    @Resource
    private IStrategyRepository repository;

    // 模拟从数据库获取的用户抽奖次数
    private Long userRaffleCount = 0L;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        String userId = ruleMatterEntity.getUserId();
        Long strategyId = ruleMatterEntity.getStrategyId();
        Long awardId = ruleMatterEntity.getAwardId();
        String ruleModel = ruleMatterEntity.getRuleModel();

        log.info("规则过滤-次数锁 userId:{} strategyId:{} awardId:{} ruleModel:{}", userId,
                strategyId, awardId, ruleModel);
        // 获取奖品配置的规则值
        String strategyRuleValue = repository.queryStrategyRuleValue(strategyId, awardId, ruleModel);
        // 次数解锁的规则值就是解锁次数
        long raffleCount = Long.parseLong(strategyRuleValue);
        if (userRaffleCount >= raffleCount) {
            // 用户抽奖次数满足解锁次数 放行
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_LOCK.getCode())
                    .build();
        }
        // 用户抽奖次数小于解锁次数 拦截
        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .ruleModel(DefaultLogicFactory.LogicModel.RULE_LOCK.getCode())
                .build();
    }
}
