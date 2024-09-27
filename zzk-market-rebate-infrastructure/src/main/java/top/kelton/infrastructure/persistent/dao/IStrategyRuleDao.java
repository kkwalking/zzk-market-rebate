package top.kelton.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kelton.infrastructure.persistent.po.StrategyRulePO;

import java.util.List;

@Mapper
public interface IStrategyRuleDao {

    List<StrategyRulePO> queryStrategyRuleList();


    StrategyRulePO queryStrategyRule(StrategyRulePO query);

    String queryStrategyRuleValue(StrategyRulePO strategyRule);
}
