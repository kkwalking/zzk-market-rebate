package top.kelton.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kelton.infrastructure.persistent.po.StrategyAwardPO;

import java.util.List;

@Mapper
public interface IStrategyAwardDao {

    List<StrategyAwardPO> queryStrategyAwardList();

    List<StrategyAwardPO> queryStrategyAwardListByStrategyId(Long strategyId);
}
