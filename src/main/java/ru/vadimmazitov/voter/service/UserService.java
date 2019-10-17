package ru.vadimmazitov.voter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.vadimmazitov.voter.AuthorizedUser;
import ru.vadimmazitov.voter.model.User;
import ru.vadimmazitov.voter.repository.UserRepository;

import static ru.vadimmazitov.voter.util.UserUtil.prepareToSave;
import static ru.vadimmazitov.voter.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

    private final UserRepository repository;
//    private final PasswordEncoder passwordEncoder;

//    TODO Put Password Encoder
    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
//        this.passwordEncoder = passwordEncoder;
    }
//Put PasswordEncoder
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(prepareToSave(user, null));
    }

    public void update(User user, int id) {
        Assert.notNull(user, "user must not be null");
        repository.save(prepareToSave(user, null));
    }

    public User get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}
