package SkillBox.com.users.dto;

public class SubscriptionDto{
        private Long id;
        private String subscriber;
        private String subscription;

        public SubscriptionDto() {
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getSubscriber() {
                return subscriber;
        }

        public void setSubscriber(String subscriber) {
                this.subscriber = subscriber;
        }

        public String getSubscription() {
                return subscription;
        }

        public void setSubscription(String subscription) {
                this.subscription = subscription;
        }
}
