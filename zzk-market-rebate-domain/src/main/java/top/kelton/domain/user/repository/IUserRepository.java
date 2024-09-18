package top.kelton.domain.user.repository;

import top.kelton.domain.user.model.entity.UserEntity;

public interface IUserRepository {

    void saveUser(UserEntity user);
}
