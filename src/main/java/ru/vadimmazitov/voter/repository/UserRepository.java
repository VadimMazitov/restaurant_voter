package ru.vadimmazitov.voter.repository;

import ru.vadimmazitov.voter.model.User;

public interface UserRepository {

//    null if not found, when updated
    User save(User user);

//    null if not found
    User get(int id);

}
