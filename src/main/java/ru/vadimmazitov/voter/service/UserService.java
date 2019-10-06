package ru.vadimmazitov.voter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.vadimmazitov.voter.model.User;
import ru.vadimmazitov.voter.repository.UserRepository;

import static ru.vadimmazitov.voter.util.UserUtil.prepareToSave;
import static ru.vadimmazitov.voter.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(prepareToSave(user, passwordEncoder));
    }

    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        repository.save(prepareToSave(user, passwordEncoder));
    }

    public User get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }


}
