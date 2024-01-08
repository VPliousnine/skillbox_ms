package SkillBox.com.users.repository;

import SkillBox.com.users.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAll();
}
