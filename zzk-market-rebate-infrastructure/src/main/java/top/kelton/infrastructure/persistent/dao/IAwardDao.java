package top.kelton.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kelton.infrastructure.persistent.po.AwardPO;

import java.util.List;

@Mapper
public interface IAwardDao {

    List<AwardPO> queryAwardList();


}
