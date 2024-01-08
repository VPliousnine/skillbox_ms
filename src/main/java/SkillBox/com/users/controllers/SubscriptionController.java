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
    String createSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.createSubscription(subscription);
    }

    @GetMapping(path = "/{id}")
    Subscription getSubscription(@PathVariable long id) {
        return subscriptionService.getSubscription(id);
    }

    @PutMapping(path = "/id")
    String updateSubscription(@RequestBody Subscription subscription, @PathVariable long id) {
        if (subscription.getId() != id) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return subscriptionService.updateSubscription(subscription, id);
    }

    @DeleteMapping(path = "/id")
    String deleteSubscription(@PathVariable long id) {
        return subscriptionService.deleteSubscription(id);
    }

    @GetMapping
    List<Subscription> getSubscriptions() {
        return subscriptionService.getSubscriptions();
    }

}
