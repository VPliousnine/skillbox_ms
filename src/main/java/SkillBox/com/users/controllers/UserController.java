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
    String create(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping(path = "/{login}")
    User get(@PathVariable String login) {
        return userService.get(login);
    }

    @PutMapping(path = "/{login}")
    String update(@RequestBody User user, @PathVariable String login) {
        if (!user.getLogin().equals(login)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return userService.update(user, login);
    }

    @DeleteMapping(path = "/{login}")
    String delete(@PathVariable String login) {
        return userService.delete(login);
    }

    @GetMapping
    List<User> getAll() {
        return userService.getAll();
    }
}
