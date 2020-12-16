package web.serviceController;

import org.springframework.stereotype.Service;
import web.model.User;

public interface Checkbox {
    void selectRoleFromCheckbox(User user, boolean isAdmin, boolean isUser);
}
