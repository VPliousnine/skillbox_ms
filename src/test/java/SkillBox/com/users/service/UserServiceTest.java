package SkillBox.com.users.service;

import SkillBox.com.users.entity.User;
import SkillBox.com.users.repository.UserRepository;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    UserService userService = new UserService(userRepository);
    String testLogin = "user";
    User savedUser = new User(testLogin, "User", "none", "phone", null, "G", "City");

    @Test
    void createUserSuccess() {
        // given
        User user = new User(testLogin, "User", "none", "phone", null, "G", "City");
        Mockito.when(userRepository.save(user)).thenReturn(savedUser);
        // when
        String result = userService.createUser(user);
        // then
        Assertions.assertEquals(String.format("Пользователь %s добавлен", testLogin), result);
    }

    @Test
    void createUserFail() {
        // given
        User user = new User(testLogin, "User", "none", "phone", null, "G", "City");
        Mockito.when(userRepository.save(user)).thenThrow(PersistenceException.class);
        // when
        Executable executable = () -> userService.createUser(user);
        // then
        Assertions.assertThrows(PersistenceException.class, executable);
    }

    @Test
    void getUserSuccess() {
        // given
        Mockito.when(userRepository.findById(testLogin)).thenReturn(Optional.of(savedUser));
        // when
        User result = userService.getUser(testLogin);
        // then
        Assertions.assertEquals(savedUser, result);
    }

    @Test
    void getUserFail() {
        // given
        Mockito.when(userRepository.findById(testLogin)).thenThrow(PersistenceException.class);
        // when
        Executable executable = () -> userService.getUser(testLogin);
        // then
        Assertions.assertThrows(PersistenceException.class, executable);
    }

    @Test
    void updateUser() throws CloneNotSupportedException {
        // given
        User modifiedUser = savedUser.clone();
        modifiedUser.setCity("NewCity");
        modifiedUser.setGender("G");

        Mockito.when(userRepository.findById(testLogin)).thenReturn(Optional.of(savedUser));
        Mockito.when(userRepository.save(modifiedUser)).thenReturn(modifiedUser);
        // when
        String result = userService.updateUser(modifiedUser, testLogin);
        // then
        Assertions.assertEquals(String.format("Пользователь %s обновлён", testLogin), result);
    }

    @Test
    void deleteUser() throws CloneNotSupportedException {
        // given
        User modifiedUser = savedUser.clone();
        modifiedUser.setDeleted(1);

        Mockito.when(userRepository.findById(testLogin)).thenReturn(Optional.of(savedUser));
        Mockito.when(userRepository.save(modifiedUser)).thenReturn(modifiedUser);
        // when
        String result = userService.deleteUser(testLogin);
        // then
        assertEquals(String.format("Пользователь %s удалён", testLogin), result);
    }

}