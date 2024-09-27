package top.kelton.domain.strategy.service.raffle;

import org.apache.commons.lang3.StringUtils;
import top.kelton.domain.strategy.model.entity.RaffleAwardEntity;
import top.kelton.domain.strategy.model.entity.RaffleFactorEntity;
import top.kelton.domain.strategy.model.entity.RuleActionEntity;
import top.kelton.domain.strategy.model.entity.StrategyEntity;
import top.kelton.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import top.kelton.domain.strategy.repository.IStrategyRepository;
import top.kelton.domain.strategy.service.armory.IStrategyDispatch;
import top.kelton.domain.strategy.service.rule.factory.DefaultLogicFactory;
import top.kelton.types.enums.ResponseCode;
import top.kelton.types.exception.AppException;

/**
 * @author zzk
 * @description 抽奖策略抽象类-定义标准抽奖流程-应用<模板方法>设计模式
 * @created 2024/9/27
 */
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    // 策略仓储服务
    protected IStrategyRepository repository;
    // 策略调度服务 只负责抽奖处理，通过新增接口的方式，隔离职责，不需要使用方关心或者调用抽奖的初始化
    protected IStrategyDispatch strategyDispatch;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
    }

    @Override
    public RaffleAwardEntity raffle(RaffleFactorEntity raffleFactorEntity) {

        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();

        // 1. 校验参数
        if (StringUtils.isBlank(userId) || strategyId == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. 抽奖前规则过滤
        // 策略查询
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);

        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).build(), strategy.ruleModels());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())) {
            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())) {
                // 黑名单返回固定的奖品ID
                return RaffleAwardEntity.builder()
                        .awardId(ruleActionEntity.getData().getAwardId())
                        .build();
            } else if (DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode().equals(ruleActionEntity.getRuleModel())) {
                // 权重根据返回的信息进行抽奖
                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionEntity.getData();
                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
                Long awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
                return RaffleAwardEntity.builder()
                        .awardId(awardId)
                        .build();
            }
        }

        // 3. 规则过滤完，执行普通随机抽奖
        Long randomAwardId = strategyDispatch.getRandomAwardId(strategyId);

        // 目前只返回奖品id
        return RaffleAwardEntity.builder()
                .strategyId(raffleFactorEntity.getStrategyId())
                .awardId(randomAwardId)
                .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);

}
