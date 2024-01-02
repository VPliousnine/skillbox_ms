package SkillBox.com.users.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscription", schema = "users_scheme")
public class Subscription {

    @Id
    long id;
    String subscriber;
    String subscription;

    public Subscription() {
    }

    public Subscription(String subscriber, String subscription) {
        this.subscriber = subscriber;
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
