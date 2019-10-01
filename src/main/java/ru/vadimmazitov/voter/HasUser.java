package ru.vadimmazitov.voter;

import ru.vadimmazitov.voter.model.User;

public interface HasUser {

    User getUser();

    void setUser(User user);

}
