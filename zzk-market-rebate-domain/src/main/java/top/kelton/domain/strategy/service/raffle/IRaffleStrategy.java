package top.kelton.domain.strategy.service.raffle;

import top.kelton.domain.strategy.model.entity.RaffleAwardEntity;
import top.kelton.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @author zzk
 * @description
 * @created 2024/9/27
 */
public interface IRaffleStrategy {

    RaffleAwardEntity raffle(RaffleFactorEntity raffleFactorEntity);
}
