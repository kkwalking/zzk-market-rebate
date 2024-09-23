package top.kelton.infrastructure.adapter.repository;

import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Service;
import top.kelton.domain.user.model.entity.UserEntity;
import top.kelton.domain.user.repository.IUserRepository;
import top.kelton.infrastructure.dao.IUserDao;
import top.kelton.infrastructure.dao.po.UserPO;
import top.kelton.infrastructure.event.EventPublisher;
import top.kelton.infrastructure.redis.IRedisService;
import top.kelton.types.event.BaseEvent;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class UserRepository implements IUserRepository {

    @Resource
    private  IRedisService redisService;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private IUserDao userDao;

    @Override
    public void saveUser(UserEntity user) {
        // 测试redis
        redisService.setValue("user:1", user);
        // 测试mq消息
        BaseEvent.EventMessage<Object> eventMessage = new BaseEvent.EventMessage<>();
        eventMessage.setId(UUID.randomUUID().toString());
        eventMessage.setTimestamp(new Date());
        eventMessage.setData(JSON.toJSONString(user));
        eventPublisher.publish("user-create", eventMessage);
        // 测试
        UserPO userPO = new UserPO();
        userPO.setUserId("zzk-1");
        userPO.setUserName("zhouzekun");
        userPO.setAge(18);
        userDao.insert(userPO);
    }
}
