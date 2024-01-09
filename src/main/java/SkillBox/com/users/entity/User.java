package SkillBox.com.users.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", schema = "users_scheme")
public class User implements Cloneable {
    @Id
    String login;
    String name;
    String interests;
    String phone;
    Integer avatar;
    String gender;
    String city;
    Integer deleted;

    public User() {
    }

    public User(String login, String name, String interests, String phone, Integer avatar, String gender, String city, Integer deleted) {
        this.login = login;
        this.name = name;
        this.interests = interests;
        this.phone = phone;
        this.avatar = avatar;
        this.gender = gender;
        this.city = city;
        this.deleted = deleted;
    }

    public User(String login, String name, String interests, String phone, Integer avatar, String gender, String city) {
        this(login, name, interests, phone, avatar, gender, city, 0);
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
                ", deleted='" + deleted + '\'' +
                '}';
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getInterests() {
        return interests;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getAvatar() {
        return avatar;
    }

    public String getGender() {
        return gender;
    }

    public String getCity() {
        return city;
    }

    public Boolean isDeleted() {
        return (deleted != null) && (deleted == 1);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted == null ? 0 : deleted;
    }

    public User clone() throws CloneNotSupportedException{
        return (User) super.clone();
    }
}
