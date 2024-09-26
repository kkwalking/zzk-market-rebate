package top.kelton.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kelton.domain.strategy.model.entity.StrategyAwardEntity;
import top.kelton.domain.strategy.model.entity.StrategyEntity;
import top.kelton.domain.strategy.model.entity.StrategyRuleEntity;
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
public class StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {
    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities =  repository.queryStrategyAwardList(strategyId);
        // 2.装配策略下全奖品概率
        assembleStrategyAward(String.valueOf(strategyId), strategyAwardEntities);

        // 3. 装配策略下规则权重奖品概率
        // 3.1 查找策略
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if (null == ruleWeight) return true;
        // 3.2 查找策略对应的权重策略配置
        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        if (null == strategyRuleEntity) {
            throw new RuntimeException("该策略id的奖品权重规则未配置");
        }
        // 3.3 构造出一个map，key是运气值，value是一个awardId的列表
        Map<String, List<Long>> ruleWeightMap = strategyRuleEntity.getRuleWeightValues();
        for (Map.Entry<String, List<Long>> entry : ruleWeightMap.entrySet()) {
            String level = entry.getKey();
            List<Long> awardIdList = entry.getValue();
            // 3.4 把不在权重奖品id列表里面的奖品id从全范围的奖品id列表过滤掉
            ArrayList<StrategyAwardEntity> ruleWeightStrategyAwards = new ArrayList<>();
            for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
                if (awardIdList.contains(strategyAwardEntity.getAwardId())) {
                    ruleWeightStrategyAwards.add(strategyAwardEntity);
                }
            }
            assembleStrategyAward(String.valueOf(strategyId).concat("_").concat(level), ruleWeightStrategyAwards);
        }

        return true;
    }


    private void assembleStrategyAward(String key, List<StrategyAwardEntity> strategyAwardEntities) {
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
        Map<Integer, Long> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < awardRateTable.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, awardRateTable.get(i));
        }
        // 3. 分布式缓存
        repository.storeStrategyAwardSearchRateTable(key, shuffleStrategyAwardSearchRateTable.size(), shuffleStrategyAwardSearchRateTable);
    }



    @Override
    public Long getRandomAwardId(Long strategyId) {
        int rateRange = repository.getRateRange(String.valueOf(strategyId));
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Long getRandomAwardId(Long strategyId, String ruleWeightValue) {
        // 策略id+规则权重值 对应的奖品概率搜索表范围已经在装配的时候存到缓存中了
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        int rateRange = repository.getRateRange(key);
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }
}
