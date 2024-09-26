package top.kelton.infrastructure.persistent.repository;

import org.springframework.stereotype.Service;
import top.kelton.domain.strategy.model.entity.StrategyAwardEntity;
import top.kelton.domain.strategy.model.entity.StrategyEntity;
import top.kelton.domain.strategy.model.entity.StrategyRuleEntity;
import top.kelton.domain.strategy.repository.IStrategyRepository;
import top.kelton.infrastructure.persistent.dao.IStrategyAwardDao;
import top.kelton.infrastructure.persistent.dao.IStrategyDao;
import top.kelton.infrastructure.persistent.dao.IStrategyRuleDao;
import top.kelton.infrastructure.persistent.po.StrategyAwardPO;
import top.kelton.infrastructure.persistent.po.StrategyPO;
import top.kelton.infrastructure.persistent.po.StrategyRulePO;
import top.kelton.infrastructure.redis.IRedisService;
import top.kelton.types.common.Constants;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 基础层-策略仓储实现
 * @author: zzk
 * @create: 2024-09-25 22:47
 **/
@Service
public class StrategyRepository implements IStrategyRepository {
    @Resource
    private IStrategyAwardDao strategyAwardDao;
    @Resource
    private IStrategyDao strategyDao;
    @Resource
    private IStrategyRuleDao strategyRuleDao;
    @Resource
    private IRedisService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if (null != strategyAwardEntities && !strategyAwardEntities.isEmpty()) return strategyAwardEntities;
        // 从库中获取数据
        List<StrategyAwardPO> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());
        for (StrategyAwardPO strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardCount(strategyAward.getAwardCount())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardRate(strategyAward.getAwardRate())
                    .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }
        redisService.setValue(cacheKey, strategyAwardEntities);
        return strategyAwardEntities;
    }

    @Override
    public void storeStrategyAwardSearchRateTable(String strategyId, int rateRange, Map<Integer, Long> strategyAwardSearchRateTable) {
        // 1. 存储抽奖策略范围
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange);
        // 2. 存储概率查找表
        Map<Integer, Long> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
        cacheRateTable.putAll(strategyAwardSearchRateTable);
    }

    @Override
    public int getRateRange(String strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

    @Override
    public Long getStrategyAwardAssemble(String key, int rateIndex) {
        Map<Integer, Long> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
        return cacheRateTable.get(rateIndex);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (null != strategyEntity) return strategyEntity;
        StrategyPO strategyPO = strategyDao.findByStrategyId(strategyId);
        strategyEntity = new StrategyEntity();
        strategyEntity.setStrategyId(strategyPO.getStrategyId());
        strategyEntity.setStrategyDesc(strategyPO.getStrategyDesc());
        strategyEntity.setRuleModels(strategyPO.getRuleModels());
        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight) {
        StrategyRulePO query = new StrategyRulePO();
        query.setStrategyId(strategyId);
        query.setRuleModel(ruleWeight);
        StrategyRulePO strategyRulePO =  strategyRuleDao.queryStrategyRule(query);
        StrategyRuleEntity strategyRuleEntity = new StrategyRuleEntity();
        strategyRuleEntity.setStrategyId(strategyRulePO.getStrategyId());
        strategyRuleEntity.setAwardId(strategyRulePO.getAwardId());
        strategyRuleEntity.setRuleType(strategyRulePO.getRuleType());
        strategyRuleEntity.setRuleModel(strategyRulePO.getRuleModel());
        strategyRuleEntity.setRuleValue(strategyRulePO.getRuleValue());
        strategyRuleEntity.setRuleDesc(strategyRulePO.getRuleDesc());
        return strategyRuleEntity;
    }
}
