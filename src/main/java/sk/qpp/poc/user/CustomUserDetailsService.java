package sk.qpp.poc.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.qpp.poc.security.UserPrincipal;
import sk.qpp.poc.security.exception.ResourceNotFoundException;
import sk.qpp.poc.user.repository.PocUser;
import sk.qpp.poc.user.repository.PocUserRepository;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService, UserDetailsPasswordService {
    private final PocUserRepository PocUserRepository;

    public CustomUserDetailsService(PocUserRepository PocUserRepository) {
        this.PocUserRepository = PocUserRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PocUser user = PocUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
                );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        PocUser user = PocUserRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }

    @Override
    @Transactional
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        UserPrincipal userCasted = (UserPrincipal) user;
        var changedRows = this.PocUserRepository.updateUsersPassword(userCasted.getId(), user.getUsername(), newPassword);
        if (changedRows == 1) {
            return new UserPrincipal(userCasted, newPassword);
        } else {
            log.warn("We have expected exactly one changed line when updating password to PocUser={}. New password={}.", user, newPassword);
            return user;
        }
    }
}
