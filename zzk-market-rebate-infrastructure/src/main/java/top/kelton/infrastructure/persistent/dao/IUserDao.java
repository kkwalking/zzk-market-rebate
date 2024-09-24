package top.kelton.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kelton.infrastructure.persistent.po.UserPO;

import java.util.List;

@Mapper
public interface IUserDao {

    void insert(UserPO userPO);
    List<UserPO> selectByUserId(String userId);

}
