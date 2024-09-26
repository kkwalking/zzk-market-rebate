package top.kelton.domain.strategy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kelton.domain.strategy.model.entity.StrategyAwardEntity;
import top.kelton.domain.strategy.repository.IStrategyRepository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 策略装配接口实现
 * @author: zzk
 * @create: 2024-09-25 20:54
 **/
@Slf4j
@Service
public class StrategyArmory implements IStrategyArmory{
    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. 取出策略相关的所有奖品
        List<StrategyAwardEntity> strategyAwardEntities =  repository.queryStrategyAwardList(strategyId);
        // 2.构造奖品概率分布
        Map<Long, BigDecimal> rateMap = strategyAwardEntities.stream().collect(Collectors.toMap(StrategyAwardEntity::getAwardId, StrategyAwardEntity::getAwardRate));
        // 2.1 获取最小概率的小数点位数
        int minScale = rateMap.values().stream()
                .min(BigDecimal::compareTo)
                .map(BigDecimal::stripTrailingZeros)
                .map(BigDecimal::scale)
                .orElse(0);
        int factor = (int)Math.pow(10, minScale);
        int rangeIndex = 0;
        Map<Integer, Long> rateToIdMap = new HashMap<>();
        for( Map.Entry<Long, BigDecimal> entry : rateMap.entrySet()) {
            Long awardId = entry.getKey();
            BigDecimal awardRate = entry.getValue();
            int rangeFrom = rangeIndex;
            int rangeTo = rangeFrom + awardRate.multiply(BigDecimal.valueOf(factor)).intValue();
            for (int range = rangeFrom; range < rangeTo; range++) {
                rateToIdMap.put(range, awardId);
            }
            rangeIndex = rangeTo+1;
        }
        // 2.2 将map中的概率顺序进行打乱
        List<Long> awardRateTable = new ArrayList<>(rateToIdMap.size());
        for (Map.Entry<Integer, Long> entry: rateToIdMap.entrySet()) {
            awardRateTable.add(entry.getValue());
        }
        Collections.shuffle(awardRateTable);
        // 生成出Map集合，key值，对应的就是后续的概率值。通过概率来获得对应的奖品ID
        Map<Integer, Long> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < awardRateTable.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, awardRateTable.get(i));
        }
        // 3. 分布式缓存
        repository.storeStrategyAwardSearchRateTable(strategyId, shuffleStrategyAwardSearchRateTable.size(), shuffleStrategyAwardSearchRateTable);
        return true;
    }

    @Override
    public Long getRandomAwardId(Long strategyId) {
        int rateRange = repository.getRateRange(strategyId);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }


    /**
     *获取target的小数部分位数(仅计算有数值部分，后缀0不计入)
     * @param target
     * @return
     */
    private int getScale(BigDecimal target) {
        // 使用 stripTrailingZeros 去除末尾的零
        BigDecimal stripped = target.stripTrailingZeros();
        // 获取小数部分位数
        int scale = stripped.scale();
        // 如果 scale 为负数，表示这个 BigDecimal 是一个整数
        return Math.max(scale, 0);
    }
}
