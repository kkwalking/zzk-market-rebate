package top.kelton.domain.strategy.service.rule.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.kelton.domain.strategy.model.entity.RuleActionEntity;
import top.kelton.domain.strategy.model.entity.RuleMatterEntity;
import top.kelton.domain.strategy.model.entity.StrategyRuleEntity;
import top.kelton.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import top.kelton.domain.strategy.repository.IStrategyRepository;
import top.kelton.domain.strategy.service.annotion.LogicStrategy;
import top.kelton.domain.strategy.service.rule.factory.DefaultLogicFactory;
import top.kelton.types.common.Constants;

import javax.annotation.Resource;

/**
 * @author zzk
 * @description
 * @created 2024/9/27
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {
    @Resource
    private IStrategyRepository repository;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        String userId = ruleMatterEntity.getUserId();

        // 1. 获取配置在数据库中的黑名单列表
        // 查询规则值配置
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Long awardId = Long.parseLong(splitRuleValue[0]);

        // 过滤其他规则
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                        .data(RuleActionEntity.RaffleBeforeEntity.builder()
                                .strategyId(ruleMatterEntity.getStrategyId())
                                .awardId(awardId)
                                .build())
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                        .build();
            }
        }

        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }
}
