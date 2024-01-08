package SkillBox.com.users;

import SkillBox.com.users.entity.User;
import SkillBox.com.users.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

	@Bean
	CommandLineRunner demoJPA(UserRepository userRepository) {
		return (args) -> {
			User user1 = new User("user1", "User1", "none", "2128506", null, "M", "Moscow");
			User user2 = new User("user2", "User2", "beer", "2128507", null, "F", "SPb");

			userRepository.save(user1);
			userRepository.save(user2);

			for (User user: userRepository.findAll()
			) {
				System.out.println(user);
			}
//			userRepository.deleteAll();

		};
	}

}
