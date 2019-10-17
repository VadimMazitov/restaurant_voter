package ru.vadimmazitov.voter.web.restaurant;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vadimmazitov.voter.View;
import ru.vadimmazitov.voter.model.Restaurant;
import ru.vadimmazitov.voter.service.RestaurantService;
import ru.vadimmazitov.voter.web.SecurityUtil;

import static ru.vadimmazitov.voter.util.ValidationUtil.checkNew;
import static ru.vadimmazitov.voter.util.ValidationUtil.assureIdConsistent;

import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = "/restaurant-voter/rest/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {

    private final Logger log = getLogger(getClass());

    private RestaurantService service;

    @Autowired
    public void setService(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return service.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> create(@Validated(View.Web.class) @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        int userId = SecurityUtil.authUserId();
        Restaurant created = service.create(userId, restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rest/restaurants/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody Restaurant restaurant, @PathVariable("id") int id) {
        log.info("update {} with id {}", restaurant, id);
        assureIdConsistent(restaurant, id);
        int userId = SecurityUtil.authUserId();
        service.update(userId, restaurant);
    }

    @GetMapping(value = "/{id}")
    public Restaurant get(@PathVariable("id") int id) {
        log.info("get restaurant with id={}", id);
        return service.get(id);
    }
}
