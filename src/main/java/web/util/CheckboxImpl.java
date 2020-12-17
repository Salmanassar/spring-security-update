package web.util;

import org.springframework.stereotype.Service;
import web.model.Role;
import web.model.User;

import java.util.*;

@Service
public class CheckboxImpl implements Checkbox {

    @Override
    public void selectRoleFromCheckbox(User user, boolean isAdmin, boolean isUser) {
        Set<Role> roles = new HashSet<>();
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (isAdmin) {
            stringJoiner.add("ROLE_ADMIN");
        }
        if (isUser) {
            stringJoiner.add("ROLE_USER");
        }
        roles.add(new Role(user.getId(), stringJoiner.toString()));
        user.setRolesList(roles);
    }
}
