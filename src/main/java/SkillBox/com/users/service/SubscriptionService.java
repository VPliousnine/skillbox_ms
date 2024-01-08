package SkillBox.com.users.service;

import SkillBox.com.users.entity.Subscription;
import SkillBox.com.users.repository.SubscriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public String createSubscription(Subscription subscription) {
        String subscriber = subscription.getSubscriber();
        String subscriptionTo = subscription.getSubscription();
        if (subscriber.equals(subscriptionTo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Подписка пользователя на самого себя невозможна");
        }
        Subscription savedSubscription = subscriptionRepository.findBySubscriberAndSubscription(subscriber, subscriptionTo);
        if (savedSubscription != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Подписка пользователя %s на пользователя %s уже есть", subscriber, subscriptionTo));
        }
        savedSubscription = subscriptionRepository.save(subscription);
        return String.format("Подписка пользователя %s на пользователя %s добавлена", savedSubscription.getSubscriber(), savedSubscription.getSubscription());
    }

    public Subscription getSubscription(long id) {
        return subscriptionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public String updateSubscription(Subscription subscription, long id) {
        if (!subscriptionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return String.format("Подписка %s обновлена", savedSubscription.getId());
    }

    public String deleteSubscription(long id) {
        if (!subscriptionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        subscriptionRepository.deleteById(id);
        return String.format("Подписка %s удалена", id);
    }

    public List<Subscription> getSubscriptions() {
        return subscriptionRepository.findAll();
    }

}
