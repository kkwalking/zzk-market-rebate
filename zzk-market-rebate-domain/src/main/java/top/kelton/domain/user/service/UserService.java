package top.kelton.domain.user.service;

import org.springframework.stereotype.Service;
import top.kelton.domain.user.repository.IUserRepository;

@Service
public class UserService {

    private final IUserRepository repository;

    public UserService(IUserRepository repository) {
        this.repository = repository;
    }

    public void saveUser(String name, Integer age) {

    }
}
