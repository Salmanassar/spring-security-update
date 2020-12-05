package web.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Fist Name is required.")
    @Column(name = "firstName")
    private String firstName;

    @NotEmpty(message = "LastName is required.")
    @Column(name = "lastName")
    private String lastName;

    @NotEmpty(message = "Email is required.")
    @Column(name = "email", unique=true)
    private String email;

    @NotEmpty(message = "Password is required.")
    @Column(name ="password")
    private String password;

    @Transient
    @NotEmpty(message = "Password is required.")
    private String passwordConfirmation;

    @Column(name = "calendar")
    private Calendar created = Calendar.getInstance();


    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
//    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    public String getRolesString() {
        return getRoles().stream().map(Object::toString).collect(Collectors.joining(", "));
    }
    public boolean isAdmin() {
        return getRolesString().contains("ROLE_ADMIN");
    }

    public boolean isUser() {
        return getRolesString().contains("ROLE_USER");
    }
}
