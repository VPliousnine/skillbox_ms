package SkillBox.com.users.controllers;

import SkillBox.com.users.entity.Subscription;
import SkillBox.com.users.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public String create(@RequestBody Subscription subscription) {
        return subscriptionService.create(subscription);
    }

    @GetMapping(path = "/{id}")
    public Subscription get(@PathVariable long id) {
        return subscriptionService.get(id);
    }

    @PutMapping(path = "/{id}")
    public String update(@RequestBody Subscription subscription, @PathVariable long id) {
        if (subscription.getId() != id) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return subscriptionService.update(subscription, id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        return subscriptionService.delete(id);
    }

    @GetMapping
    public List<Subscription> getAll() {
        return subscriptionService.getAll();
    }

}
