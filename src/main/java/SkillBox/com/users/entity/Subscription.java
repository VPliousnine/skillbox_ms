package SkillBox.com.users.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subscriptions", schema = "users_scheme")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subscriber;
    private String subscription;

    public Subscription() {
    }

    public Subscription(String subscriber, String subscription) {
        this.subscriber = subscriber;
        this.subscription = subscription;
    }

    public Subscription(long id, String subscriber, String subscription) {
        this.id = id;
        this.subscriber = subscriber;
        this.subscription = subscription;
    }

    public long getId() {
        return id;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", subscriber='" + subscriber + '\'' +
                ", subscription='" + subscription + '\'' +
                '}';
    }
}
