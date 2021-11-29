package sk.qpp.poc.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import sk.qpp.poc.user.repository.PocUser;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class does primary hold {@link #id}, {@link #email} and {@link #password} (hash).
 * <p>
 * NOTE: {@link #getName()} method returns {@link #email} value. This is our primary identification of the user
 */
public class UserPrincipal implements OAuth2User, UserDetails {
    @Getter
    private final Long id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public UserPrincipal(UserPrincipal user, String password) {
        this.id = user.getId();
        this.email = user.getUsername();
        this.password = password;
        this.authorities = user.getAuthorities();
    }

    public static UserPrincipal create(PocUser user) {
        // TODO load roles here?! I think not. Somewhere there is Group managing think and so on.
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        // Instance will be used by ProviderManager instance (that with DaoAuthenticationProvider instance in providers
        // list), to validate password using PasswordEncoder set up to it in AuthenticationManagerBuilder. See
        // SecurityConfig, line 63. Passing HASHED password is necessary!
        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPasswordSaltedHashed(),
                authorities
        );
    }

    public static UserPrincipal create(PocUser user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
