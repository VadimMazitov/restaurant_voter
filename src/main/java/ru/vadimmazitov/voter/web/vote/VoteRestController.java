package ru.vadimmazitov.voter.web.vote;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vadimmazitov.voter.View;
import ru.vadimmazitov.voter.model.Vote;
import ru.vadimmazitov.voter.service.VoteService;
import ru.vadimmazitov.voter.web.SecurityUtil;

import java.net.URI;
import java.util.List;

import static ru.vadimmazitov.voter.util.ValidationUtil.checkNew;
import static ru.vadimmazitov.voter.util.ValidationUtil.assureIdConsistent;
import static org.slf4j.LoggerFactory.getLogger;

//TODO change @Validated to @Valid (check from topjava project)
@RestController
@RequestMapping(value = "/rest/{restaurantId}/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    private final Logger log = getLogger(getClass());

    private VoteService service;

    @Autowired
    public void setService(VoteService service) {
        this.service = service;
    }

    @SuppressWarnings("Duplicates")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> create(@Validated(View.Web.class) @RequestBody Vote vote, @PathVariable("restaurantId") int restaurantId) {
        log.info("create {}", vote);
        checkNew(vote);
        int userId = SecurityUtil.authUserId();
        Vote created = service.create(userId, restaurantId, vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rest/" + restaurantId + "/meals/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody Vote vote, @PathVariable("id") int id) {
        log.info("update {} with id={}", vote, id);
        assureIdConsistent(vote, id);
        int userId = SecurityUtil.authUserId();
        service.update(userId, vote);
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable("id") int id) {
        log.info("get with id={}", id);
        int userId = SecurityUtil.authUserId();
        return service.get(userId, id);
    }

    @GetMapping
    public List<Vote> getAllForRestaurant(@PathVariable("restaurantId") int restaurantId) {
        log.info("get all for restaurant with id={}", restaurantId);
        return service.getAllForRestaurant(restaurantId);
    }

    @GetMapping("/forUser")
    public List<Vote> getAllForUser() {
        int userId = SecurityUtil.authUserId();
        log.info("get all for user with id={}", userId);
        return service.getAllForUser(userId);
    }
}

