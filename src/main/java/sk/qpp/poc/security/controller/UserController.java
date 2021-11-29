package sk.qpp.poc.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.qpp.poc.security.UserPrincipal;
import sk.qpp.poc.security.exception.ResourceNotFoundException;
import sk.qpp.poc.user.repository.PocUser;
import sk.qpp.poc.user.repository.PocUserRepository;

@RestController
public class UserController {

    @Autowired
    private PocUserRepository PocUserRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public PocUser getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return PocUserRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
