package SkillBox.com.users.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", schema = "users_scheme")
public class User {
    @Id
    String login;
    String name;
    String interests;
    String phone;
    Integer avatar;
    String gender;
    String city;

    public User() {
    }

    public User(String login, String name, String interests, String phone, Integer avatar, String gender, String city) {
        this.login = login;
        this.name = name;
        this.interests = interests;
        this.phone = phone;
        this.avatar = avatar;
        this.gender = gender;
        this.city = city;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", interests='" + interests + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar=" + avatar +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
