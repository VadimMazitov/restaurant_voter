package ru.vadimmazitov.voter.web.user;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vadimmazitov.voter.AuthorizedUser;
import ru.vadimmazitov.voter.model.User;
import ru.vadimmazitov.voter.service.UserService;

import javax.validation.Valid;

import static ru.vadimmazitov.voter.util.ValidationUtil.checkNew;
import static ru.vadimmazitov.voter.util.ValidationUtil.assureIdConsistent;

import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(UserRestController.REST_URL)
public class UserRestController {

    static final String REST_URL = "/rest/user";

    private final Logger log = getLogger(getClass());

    private UserService service;

    @Autowired
    public void setService(UserService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("get with id={}", authUser.getId());
        return service.get(authUser.getId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        log.info("create {}", user);
        checkNew(user);
        User created = service.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("update {} with id={}", user, authUser.getId());
        assureIdConsistent(user, authUser.getId());
        service.update(user, authUser.getId());
    }


}
