package top.kelton.domain.strategy.service.armory;

/**
 * 策略装配
 */
public interface IStrategyArmory {

    /**
     * 装配抽奖策略配置「触发的时机可以为活动审核通过后进行调用」
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    boolean assembleLotteryStrategy(Long strategyId);

    // /**
    //  * 策略id+规则权重装配活动策略
    //  * @param strategyId 策略id
    //  * @param ruleWeight 规则权重。规则权重格式如“6000:102,103,104,105,106,107,108”
    //  *                   规则权重用于指定当用户运气值到达某一档位时转盘可抽奖的奖品列表
    //  * @return
    //  */
    // boolean assembleLotteryStrategy(Long strategyId, String ruleWeight);

}