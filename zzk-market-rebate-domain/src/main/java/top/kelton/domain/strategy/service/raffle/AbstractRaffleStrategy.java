package top.kelton.domain.strategy.service.raffle;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import top.kelton.domain.strategy.model.entity.RaffleAwardEntity;
import top.kelton.domain.strategy.model.entity.RaffleFactorEntity;
import top.kelton.domain.strategy.model.entity.RuleActionEntity;
import top.kelton.domain.strategy.model.entity.StrategyEntity;
import top.kelton.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import top.kelton.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
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
@Slf4j
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

        // 顺序过滤，被其中一个接管则跳出过滤链进行返回，也可能没有规则接管
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = this.doCheckRaffleBeforeLogic(
                RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).build(), strategy.ruleModels());
        // 抽象出方法，统一处理接管结果的转换
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
        log.info("未被前置规则接管，继续执行 strategyId:{}, userId:{}", strategyId, userId);

        // 3. 规则过滤完，执行普通随机抽奖
        Long awardId = strategyDispatch.getRandomAwardId(strategyId);
        log.info("随机抽奖结果 strategyId:{} userId:{} awardId:{}", strategyId, userId, awardId);

        // 4. 查询奖品抽奖中、后规则  [抽奖中（拿到奖品ID时，过滤规则）、抽奖后（扣减完奖品库存后过滤，抽奖中拦截和无库存则走兜底）]
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        String[] centerRuleModelList = strategyAwardRuleModelVO.raffleCenterRuleModelList();
        log.info("执行抽奖中置规则 strategyId:{} userId:{} awardId:{} centerRuleModelList:{}",
                strategyId, userId, awardId, JSON.toJSONString(centerRuleModelList));
        // 4.1 抽奖中置规则过滤
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
                .userId(userId)
                .strategyId(strategyId)
                .awardId(awardId)
                .build(), centerRuleModelList);
        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())) {
            log.info("抽奖中置规则拦截，strategyId:{} userId:{} awardId:{} ruleModel:{}",
                    strategyId, userId, awardId, ruleActionCenterEntity.getRuleModel());
            return RaffleAwardEntity.builder()
                    .awardDesc("抽奖中置规则拦截，后续通过抽奖后置规则 rule_luck_award 走兜底奖励。")
                    .build();
        }
        // 4.2 后续会有后置规则
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);

}
