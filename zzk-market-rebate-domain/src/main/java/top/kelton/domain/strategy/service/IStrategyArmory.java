package top.kelton.domain.strategy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description: 策略装配接口
 * @author: zzk
 * @create: 2024-09-25 20:50
 **/

public interface IStrategyArmory {

    boolean assembleLotteryStrategy(Long strategyId);

    Long getRandomAwardId(Long strategyId);

}
