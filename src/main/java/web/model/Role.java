package web.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Component
@NoArgsConstructor
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roles", nullable = false)
    private String role;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role(Long id, String role) {
        this.id=id;
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return getRole();
    }
}
