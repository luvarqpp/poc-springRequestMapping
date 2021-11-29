package sk.qpp.poc.user.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Table;
import sk.qpp.poc.user.AuthProvider;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@javax.persistence.Table(
        name = "poc_users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@Table(appliesTo = "poc_users", comment = "poc_users table holds information and optionally credentials, for all " +
        "users of poc software.")
public class PocUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * For {@link AuthProvider#LOCAL} provider, here is login name. For other providers, here we have name which
     * is provided by third party profile, i.e. github or Google.
     */
    @Column(unique = true, length = 256, nullable = false, insertable = true, updatable = true)
    @Size(min = 2, max = 256, message = "displayName of PocUser should have at least 2 characters and not exceed 256 characters!")
    @NotNull
    private String displayName;

    @NotNull
    @Email
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    /**
     * This field hold salted and hashed password from user singup/login requests.
     * <p>
     * NOTE: This field holds also other data, like used salt and hash version/parameters. It should by design get
     * result of {@link org.springframework.security.crypto.password.PasswordEncoder#encode(CharSequence)} method.
     */
    @JsonIgnore
    private String passwordSaltedHashed;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<PocUserRole> pocUserRoles = new HashSet<>();

    /**
     * Create new {@link AuthProvider#LOCAL} {@link PocUser} instance.
     *
     * @param displayName name, which is stored only for information and it is at least used to display information
     *                    about logged user in system
     * @param email       email of given {@link PocUser}. It will be used as login name for local users
     */
    PocUser(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
        this.provider = AuthProvider.LOCAL;
    }

    /**
     * Create new {@link AuthProvider#LOCAL} {@link PocUser} instance.
     *
     * @param displayName          name, which is stored only for information and it is at least used to display
     *                             information about logged user in system
     * @param email                email of given {@link PocUser}. It will be used as login name for local users
     * @param passwordSaltedHashed password string from
     *                             {@link org.springframework.security.crypto.password.PasswordEncoder} implementation,
     *                             which does hold its version, salt value and also salted+hashed password
     */
    PocUser(String displayName, String email, String passwordSaltedHashed) {
        this(displayName, email);
        this.setPasswordSaltedHashed(passwordSaltedHashed);
    }
}
