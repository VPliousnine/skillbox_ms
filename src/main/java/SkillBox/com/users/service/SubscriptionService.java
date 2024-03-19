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

    public String create(Subscription subscription) {
        String subscriber = subscription.getSubscriber();
        String subscriptionTo = subscription.getSubscription();
        if (subscriber.equals(subscriptionTo)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Подписка пользователя на самого себя невозможна");
        }
        Subscription savedSubscription = subscriptionRepository.findBySubscriberAndSubscription(subscriber, subscriptionTo);
        if (savedSubscription != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Подписка пользователя %s на пользователя %s уже есть", subscriber, subscriptionTo));
        }
        savedSubscription = subscriptionRepository.save(subscription);
        return String.format("Подписка пользователя %s на пользователя %s добавлена", savedSubscription.getSubscriber(), savedSubscription.getSubscription());
    }

    public Subscription get(long id) {
        return subscriptionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public String update(Subscription subscription, long id) {
        if (!subscriptionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return String.format("Подписка %s обновлена", savedSubscription.getId());
    }

    public String delete(long id) {
        if (!subscriptionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        subscriptionRepository.deleteById(id);
        return String.format("Подписка %s удалена", id);
    }

    public List<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

//    public static SubscriptionDto mapToDto(Subscription subscription) {
//        SubscriptionDto subscriptionDto = new SubscriptionDto();
//        subscriptionDto.setId(subscription.getId());
//        subscriptionDto.setSubscriber(subscription.getSubscriber());
//        subscriptionDto.setSubscription(subscription.getSubscription());
//        return subscriptionDto;
//    }
//
//    public static Subscription mapToEntity(SubscriptionDto subscriptionDto) {
//        Subscription subscription = new Subscription();
//        subscription.setId(subscriptionDto.getId());
//        subscription.setSubscriber(subscriptionDto.getSubscriber());
//        subscription.setSubscription(subscriptionDto.getSubscription());
//        return subscription;
//    }
}
