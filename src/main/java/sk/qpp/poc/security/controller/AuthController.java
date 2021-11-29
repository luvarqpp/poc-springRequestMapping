package sk.qpp.poc.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.qpp.poc.security.TokenProvider;
import sk.qpp.poc.security.UserPrincipal;
import sk.qpp.poc.security.controller.payload.AuthResponse;
import sk.qpp.poc.security.controller.payload.LoginRequest;
import sk.qpp.poc.security.controller.payload.SignUpRequest;
import sk.qpp.poc.security.exception.BadRequestException;
import sk.qpp.poc.user.repository.PocUser;
import sk.qpp.poc.user.repository.PocUserFactoryService;
import sk.qpp.poc.user.repository.PocUserRepository;

import javax.validation.Valid;
import java.net.URI;

@RepositoryRestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final PocUserRepository PocUserRepository;

    private final PocUserFactoryService PocUserFactoryService;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;
    private final PagedResourcesAssembler<PocUser> pagedAssembler;
    private final EntityLinks entityLinks;

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("New login request received: {}", loginRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String token = tokenProvider.createToken(userPrincipal.getId());
        final AuthResponse authResponse = new AuthResponse(token);
        log.debug("Login successful, response: {}", authResponse);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignUpRequest signUpRequest, PersistentEntityResourceAssembler resourceAssembler) {
        log.debug("signup request: {}", signUpRequest);
        // Creating user's account. This does take a few more moments. It is computing password hash
        final PocUser newUser = PocUserFactoryService.createLocalUser(this.passwordEncoder, signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getPassword());

        // checking existence of email just before going to save new account. // TODO There is chance, that two users at once hit this and one will get constrain error on insert on save call and thus will see different error message.
        if (PocUserRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // save to database
        final PocUser savedEntity = PocUserRepository.save(newUser);

        final PersistentEntityResource newEntityResource = resourceAssembler.toModel(savedEntity);
        Link link = entityLinks.linkToItemResource(PocUser.class, savedEntity.getId()).expand();
        log.debug("signup successful, links to newly created resource: {}", link.getHref());
        return ResponseEntity
                .created(URI.create(link.getHref()))
                .body(newEntityResource);
    }
}
