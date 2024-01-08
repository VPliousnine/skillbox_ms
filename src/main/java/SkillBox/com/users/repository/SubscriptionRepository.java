package SkillBox.com.users.repository;

import SkillBox.com.users.entity.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    List<Subscription> findAll();
    Subscription findBySubscriberAndSubscription(String subscriber, String subscription);
}
