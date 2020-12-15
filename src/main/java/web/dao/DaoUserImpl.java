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
    public User readUser(Long id) {
        return (User) entityManager.createQuery("select u from User u join fetch u.roles where u.id=:id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void deleteUser(Long id) {
        User user = readUser(id);
        entityManager.remove(user);
    }

    @Override
    public List<User> listUsers() {
        TypedQuery<User> query =
                entityManager.createQuery("select u from User u join fetch u.roles", User.class);
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
        } else if (findUserByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("There is the user with the same email");
        }
        else if (user.getRoles()== null) {
          setRoleIfNull(user, EnumRoles.ROLE_USER.name());
        }
        passwordEncoder.encode(user.getPassword());
        return entityManager.merge(user);
    }

    @Override
    public User updateUser(User user) {
        User updateUser = getUserById(user.getId()).get();
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getFirstName());
        updateUser.setCreated(user.getCreated());
        updateUser.setEmail(user.getEmail());
        updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRoles()==null){
            setRoleIfNull(updateUser,EnumRoles.ROLE_USER.name());
        }
        updateUser.setRolesList(user.getRoles());
        user = updateUser;
        entityManager.persist(user);
        return user;
    }

    @Override
    public List<Role> rolesList() {
        TypedQuery<Role> query =
                entityManager.createQuery("SELECT u FROM Role u", Role.class);
        return query.getResultList();
    }

    private boolean isEmptyFields(User user) {
        return Stream.of(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword())
                .noneMatch(String::isEmpty);
    }

    private void setRoleIfNull(User user, String role){
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(user.getId(),role));
        user.setRolesList(roles);
    }
}
