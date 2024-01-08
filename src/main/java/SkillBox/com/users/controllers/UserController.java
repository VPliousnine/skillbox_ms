package SkillBox.com.users.controllers;

import SkillBox.com.users.entity.User;
import SkillBox.com.users.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    String createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping(path = "/{login}")
    User getUser(@PathVariable String login) {
        return userService.getUser(login);
    }

    @PutMapping(path = "/login")
    String updateUser(@RequestBody User user, @PathVariable String login) {
        if (!user.getLogin().equals(login)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return userService.updateUser(user, login);
    }

    @DeleteMapping(path = "/login")
    String deleteUser(@PathVariable String login) {
        return userService.deleteUser(login);
    }

    @GetMapping
    List<User> getUsers() {
        return userService.getUsers();
    }
}
