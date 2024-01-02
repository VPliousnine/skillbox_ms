package SkillBox.com.users.repository;

import SkillBox.com.users.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
