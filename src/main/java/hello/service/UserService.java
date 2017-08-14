package hello.service;

import hello.model.Role;
import hello.model.User;

public interface UserService {
    User findUserByEmail(String email);
    void saveUser(User user, Role role);
}
