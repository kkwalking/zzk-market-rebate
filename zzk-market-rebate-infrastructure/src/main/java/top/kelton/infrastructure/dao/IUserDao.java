package top.kelton.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kelton.infrastructure.dao.po.UserOrderPO;
import top.kelton.infrastructure.dao.po.UserPO;

import java.util.List;

@Mapper
public interface IUserDao {

    void insert(UserPO userPO);
    List<UserPO> selectByUserId(String userId);

}
