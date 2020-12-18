package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import web.model.EnumRoles;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Stream;

@Repository
public class DaoUserImpl implements DaoUser {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id).orElseGet(User::new);
        entityManager.remove(user);
    }

    @Override
    public List<User> listUsers() {
        TypedQuery<User> query = entityManager.createQuery(
                "select u from User u join fetch u.roles", User.class);
        return query.getResultList();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        Stream<User> streamUser = entityManager.createQuery(
                "select u from User u join fetch u.roles where u.email = :email", User.class).
                setParameter("email", email).getResultStream();
        User user = streamUser.findFirst().orElse(null);
        return user!= null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public User createUser(User user) {
        if (!isEmptyFields(user)) {
            throw new RuntimeException("There is an empty field(s)");
        }
        else if (findUserByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("There is the user with the same email");
        }
        passwordEncoder.encode(user.getPassword());
        return entityManager.merge(user);
    }

    @Override
    public User updateUser(User user) {
        if (!isEmptyFields(user)) {
            throw new RuntimeException("There is an empty field(s)");
        }
        return entityManager.merge(user);
    }

    private boolean isEmptyFields(User user) {
        return Stream.of(user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPassword(), user.getRolesString())
                .noneMatch(String::isEmpty);
    }

    private void setRoleIfNull(User user, String role){
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(user.getId(),role));
        user.setRoles(roles);
    }
}
