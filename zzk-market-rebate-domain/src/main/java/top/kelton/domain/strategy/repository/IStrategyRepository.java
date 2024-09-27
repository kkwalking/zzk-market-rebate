package top.kelton.domain.strategy.repository;


import top.kelton.domain.strategy.model.entity.StrategyAwardEntity;
import top.kelton.domain.strategy.model.entity.StrategyEntity;
import top.kelton.domain.strategy.model.entity.StrategyRuleEntity;

import java.util.List;
import java.util.Map;

/**
 * 策略仓储接口
 */
public interface IStrategyRepository {
    List<StrategyAwardEntity>  queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(String strategyId, int rateRange, Map<Integer, Long> strategyAwardSearchRateTable);

    /**
     * 获取策略对应的概率范围
     * @param strategyId
     * @return
     */
    int getRateRange(String strategyId);

    /**
     * 获取该策略对应index的奖品id
     * @param key
     * @param rateIndex
     * @return
     */
    Long getStrategyAwardAssemble(String key,  int rateIndex);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

}
