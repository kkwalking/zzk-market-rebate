package top.kelton.domain.strategy.model.entity;

import lombok.*;
import top.kelton.domain.strategy.model.valobj.RuleLogicCheckTypeVO;

/**
 * @author zzk
 * @description
 * @created 2024/9/27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity>{


    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;
    static public class RaffleEntity {

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity {
        /**
         * 策略ID
         */
        private Long strategyId;

        /**
         * 权重值Key；用于抽奖时可以选择权重抽奖。
         */
        private String ruleWeightValueKey;

        /**
         * 奖品ID；
         */
        private Long awardId;
    }

    // 抽奖之中
    static public class RaffleCenterEntity extends RaffleEntity {
        /**
         * 策略ID
         */
        private Long strategyId;

        private Long userId;

    }

    // 抽奖之后
    static public class RaffleAfterEntity extends RaffleEntity {

    }

}
