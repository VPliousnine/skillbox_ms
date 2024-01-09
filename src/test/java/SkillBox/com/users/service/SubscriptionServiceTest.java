package SkillBox.com.users.service;

import SkillBox.com.users.entity.Subscription;
import SkillBox.com.users.repository.SubscriptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

class SubscriptionServiceTest {

    SubscriptionRepository subscriptionRepository = Mockito.mock(SubscriptionRepository.class);
    SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);
    long testId = 1L;
    Subscription savedSubscription = new Subscription(testId,"user1", "user2");

    @Test
    void createSubscription() {
        // given
        Subscription subscription = new Subscription("user1", "user2");
        Mockito.when(subscriptionRepository.save(subscription)).thenReturn(savedSubscription);
        // when
        String result = subscriptionService.createSubscription(subscription);
        // then
        Assertions.assertEquals("Подписка пользователя user1 на пользователя user2 добавлена", result);
    }

    @Test
    void getSubscription() {
        // given
        Mockito.when(subscriptionRepository.findById(testId)).thenReturn(Optional.of(savedSubscription));
        // when
        Subscription result = subscriptionService.getSubscription(testId);
        // then
        Assertions.assertEquals(savedSubscription, result);
    }

    @Test
    void updateSubscription() {
        // given
        Subscription modifiedSubscription = new Subscription(testId, "user1", "user3");

        Mockito.when(subscriptionRepository.existsById(testId)).thenReturn(true);
        Mockito.when(subscriptionRepository.save(modifiedSubscription)).thenReturn(modifiedSubscription);
        // when
        String result = subscriptionService.updateSubscription(modifiedSubscription, testId);
        // then
        Assertions.assertEquals(String.format("Подписка %s обновлена", testId), result);
    }

    @Test
    void deleteSubscription() {
        // given
        Subscription modifiedSubscription = new Subscription(testId, "user1", "user3");

        Mockito.when(subscriptionRepository.existsById(testId)).thenReturn(true);
        // when
        String result = subscriptionService.deleteSubscription(testId);
        // then
        Assertions.assertEquals(String.format("Подписка %s удалена", testId), result);
    }
}