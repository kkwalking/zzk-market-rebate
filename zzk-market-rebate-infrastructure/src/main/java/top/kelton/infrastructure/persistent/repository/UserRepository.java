package top.kelton.infrastructure.persistent.repository;

import org.springframework.stereotype.Service;
import top.kelton.domain.user.model.entity.UserEntity;
import top.kelton.domain.user.repository.IUserRepository;
import top.kelton.infrastructure.redis.IRedisService;

@Service
public class UserRepository implements IUserRepository {

    private final IRedisService redisService;

    public UserRepository(IRedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void saveUser(UserEntity user) {
        redisService.setValue("user:1", user);
    }
}
