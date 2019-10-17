package ru.vadimmazitov.voter.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vadimmazitov.voter.View;
import ru.vadimmazitov.voter.model.Meal;
import ru.vadimmazitov.voter.service.MealService;
import ru.vadimmazitov.voter.web.SecurityUtil;

import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.vadimmazitov.voter.util.ValidationUtil.checkNew;
import static ru.vadimmazitov.voter.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = "/rest/restaurants/{restaurantId}/meals", produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController {

    private final Logger log = getLogger(getClass());

    private MealService service;

    @Autowired
    public void setService(MealService service) {
        this.service = service;
    }

    @GetMapping
    public List<Meal> getAll(@PathVariable("restaurantId") int restaurantId) {
        return service.getAllForRestaurant(restaurantId);
    }

    @GetMapping(value = "/{id}")
    public Meal get(@PathVariable("id")int id) {
        log.info("get with id{}", id);
        return service.get(id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id, @PathVariable("restaurantId") int restaurantId) {
        log.info("delete with id={}", id);
        int userId = SecurityUtil.authUserId();
        service.delete(userId, restaurantId, id);
    }

    /*
    https://stackoverflow.com/a/52092984 - difference between @Valid and @Validated
    */
    @SuppressWarnings("Duplicates")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Meal> create(@Validated(View.Web.class) @RequestBody Meal meal, @PathVariable("restaurantId") int restaurantId) {
        log.info("create {}", meal);
        checkNew(meal);
        int userId = SecurityUtil.authUserId();
        Meal created = service.create(userId, restaurantId, meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rest/" + restaurantId + "/meals/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody Meal meal, @PathVariable("id") int id, @PathVariable("restaurantId") int restaurantId) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        int userId = SecurityUtil.authUserId();
        service.update(userId, restaurantId, meal);
    }

}
