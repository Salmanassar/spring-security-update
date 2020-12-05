package web.service;

import org.springframework.stereotype.Service;
import web.model.Role;
import web.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface UserService {

    User createUser(User user);

    User readUser(Long id);

    void updateUser(User user);

    void deleteUser(Long id);

    List<User> listUsers();

    Optional <User> findUserByEmail(String email);

    User getUserById(Long id);

    List<Role> rolesList();
}
