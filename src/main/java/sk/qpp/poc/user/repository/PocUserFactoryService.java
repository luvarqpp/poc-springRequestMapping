package sk.qpp.poc.user.repository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.qpp.poc.user.AuthProvider;

import java.util.Set;

@Service
public class PocUserFactoryService {
    public PocUser createExternalUser(String displayName, String email, String externalProviderId, AuthProvider authProvider) {
        PocUser PocUser = new PocUser(displayName, email);
        PocUser.setProviderId(externalProviderId);
        PocUser.setProvider(authProvider);
        return PocUser;
    }

    public PocUser createLocalUser(PasswordEncoder passwordEncoder, String displayName, String email, Set<PocUserRole> pocUserRoles, String password) {
        PocUser PocUser = this.createLocalUser(passwordEncoder, displayName, email, password);
        PocUser.setPocUserRoles(pocUserRoles);
        return PocUser;
    }

    /**
     * Create {@link PocUser} instance with filled basic fields and also correctly stored password (salted and
     * hashed).
     *
     * @param displayName
     * @param email
     * @param password
     * @return
     */
    public PocUser createLocalUser(PasswordEncoder passwordEncoder, String displayName, String email, String password) {
        final String pwdHash = passwordEncoder.encode(password);
        PocUser PocUser = new PocUser(displayName, email, pwdHash);
        return PocUser;
    }
}
