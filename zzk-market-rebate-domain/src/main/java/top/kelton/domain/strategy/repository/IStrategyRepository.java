package top.kelton.domain.strategy.repository;


import top.kelton.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;
import java.util.Map;

/**
 * 策略仓储接口
 */
public interface IStrategyRepository {
    List<StrategyAwardEntity>  queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(Long strategyId, int rateRange, Map<Integer, Long> strategyAwardSearchRateTable);

    /**
     * 获取策略对应的概率范围
     * @param strategyId
     * @return
     */
    int getRateRange(Long strategyId);

    /**
     * 获取该策略对应index的奖品id
     * @param strategyId
     * @param rateIndex
     * @return
     */
    Long getStrategyAwardAssemble(Long strategyId, int rateIndex);
}
