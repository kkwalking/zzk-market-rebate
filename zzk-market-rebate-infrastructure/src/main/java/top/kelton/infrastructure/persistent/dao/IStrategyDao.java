package top.kelton.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kelton.infrastructure.persistent.po.StrategyPO;

import java.util.List;

@Mapper
public interface IStrategyDao {

    List<StrategyPO> queryStrategyList();

    StrategyPO findByStrategyId(Long strategyId);
}
