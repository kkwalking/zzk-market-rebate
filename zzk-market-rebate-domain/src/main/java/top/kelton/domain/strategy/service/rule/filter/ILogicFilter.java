package top.kelton.domain.strategy.service.rule.filter;

import top.kelton.domain.strategy.model.entity.RuleActionEntity;
import top.kelton.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @author zzk
 * @description 抽奖规则过滤接口
 * @created 2024/9/27
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);

}
