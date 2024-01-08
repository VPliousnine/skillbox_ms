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

    public String createUser(User user) {
        String login = user.getLogin();
        if (userRepository.existsById(login)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Пользователь %s уже есть", login));
        }
        User savedUser = userRepository.save(user);
        return String.format("Пользователь %s добавлен", savedUser.getLogin());
    }

    public User getUser(String login) {
        User user = userRepository.findById(login).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public String updateUser(User user, String login) {
        User savedUser = userRepository.findById(login).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (savedUser.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        savedUser = userRepository.save(user);
        return String.format("Пользователь %s обновлён", savedUser.getLogin());
    }

    public String deleteUser(String login) {
        User user = userRepository.findById(login).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        user.setDeleted(1);
        userRepository.save(user);
        return String.format("Пользователь %s удалён", login);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
