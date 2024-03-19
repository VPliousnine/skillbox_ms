package SkillBox.com.users.service;

import SkillBox.com.users.entity.User;
import SkillBox.com.users.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String create(User user) {
        String login = user.getLogin();
        if (userRepository.existsById(login)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, String.format("Пользователь %s уже есть", login));
        }
        User savedUser = userRepository.save(user);
        return String.format("Пользователь %s добавлен", savedUser.getLogin());
    }

    public User get(String login) {
        User user = userRepository.findById(login).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public String update(User user, String login) {
        User savedUser = userRepository.findById(login).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (savedUser.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        savedUser = userRepository.save(user);
        return String.format("Пользователь %s обновлён", savedUser.getLogin());
    }

    public String delete(String login) {
        User user = userRepository.findById(login).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        user.setDeleted(1);
        userRepository.save(user);
        return String.format("Пользователь %s удалён", login);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

//    public static UserDto mapToDto(User user) {
//        UserDto userDto = new UserDto();
//        userDto.setLogin(user.getLogin());
//        userDto.setName(user.getName());
//        userDto.setInterests(user.getInterests());
//        userDto.setPhone(user.getPhone());
//        userDto.setAvatar(user.getAvatar());
//        userDto.setGender(user.getGender());
//        userDto.setCity(user.getCity());
//        userDto.setDeleted(user.getDeleted());
//        return userDto;
//    }
//
//    public static User mapToEntity(UserDto userDto) {
//        User user = new User();
//        user.setLogin(userDto.getLogin());
//        user.setName(userDto.getName());
//        user.setInterests(userDto.getInterests());
//        user.setPhone(userDto.getPhone());
//        user.setAvatar(userDto.getAvatar());
//        user.setGender(userDto.getGender());
//        user.setCity(userDto.getCity());
//        user.setDeleted(userDto.getDeleted());
//        return user;
//    }

}
