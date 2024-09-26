package top.kelton.domain.strategy.service.armory;

/**
 * 策略抽奖调用
 */
public interface IStrategyDispatch {

    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 策略ID
     * @return 抽奖结果
     */
    Long getRandomAwardId(Long strategyId);

    /**
     * 权重抽奖
     * @param strategyId
     * @param ruleWeightValue
     * @return
     */
    Long getRandomAwardId(Long strategyId, String ruleWeightValue);

}