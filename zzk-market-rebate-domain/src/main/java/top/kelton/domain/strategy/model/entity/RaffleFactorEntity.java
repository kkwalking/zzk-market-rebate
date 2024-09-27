package top.kelton.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzk
 * @description
 * @created 2024/9/27
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleFactorEntity {


    /** 用户ID */
    private String userId;
    /** 策略ID */
    private Long strategyId;
}
