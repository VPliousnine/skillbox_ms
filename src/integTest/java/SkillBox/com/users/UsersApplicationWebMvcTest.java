package SkillBox.com.users;

import SkillBox.com.users.dto.SubscriptionDto;
import SkillBox.com.users.dto.UserDto;
import SkillBox.com.users.entity.Subscription;
import SkillBox.com.users.entity.User;
import SkillBox.com.users.repository.SubscriptionRepository;
import SkillBox.com.users.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersApplicationWebMvcTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    TestRestTemplate template = new TestRestTemplate();

    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres");

    @BeforeAll
    public static void beforeAll() {
        postgresContainer.start();
    }

    @AfterAll
    public static void AfterAll() {
        postgresContainer.stop();
    }

    @DynamicPropertySource
    public static void ConfigureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    public void fillDatabase() {
        userRepository.save(new User("user1", "User1", "none", "2128506", null, "M", "Moscow"));
        userRepository.save(new User("user2", "User2", "beer", "2128507", null, "F", "SPb"));
        userRepository.save(new User("user5", "User5", "vodka", "212850a", null, "T", "LA"));
        userRepository.save(new User("user6", "User6", "absinth", "212850b", null, "T", "Paris"));
        subscriptionRepository.save(new Subscription("user1", "user2"));
        subscriptionRepository.save(new Subscription("user1", "user5"));
    }

    @Test
    @DisplayName("Test get user by id")
    public void testGetUserById() {
        ResponseEntity<UserDto> response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/users/user1", UserDto.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("2128506", response.getBody().getPhone());
        System.out.println("Выполнен тест чтения пользователя из БД");
    }

    @Test
    @DisplayName("Test get user 404")
    public void testGetUserById404() {
        ResponseEntity<UserDto> response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/users/user0", UserDto.class);
        assertEquals(404, response.getStatusCode().value());
        System.out.println("Выполнен тест неуспешного чтения пользователя - запись не найдена");
    }

    @Test
    @DisplayName("Test create user")
    public void testSaveUser() {
        User user = new User("user3", "User3", "whiskey", "2128508", null, "T", "Wa");
        ResponseEntity<String> textResponse = template
                .postForEntity("http://localhost:" + port + "/users", user, String.class);
        assertEquals("Пользователь user3 добавлен", textResponse.getBody());
        System.out.println("Выполнен тест создания пользователя");
    }

    @Test
    @DisplayName("Test create user duplicate")
    public void testCreateUserDuplicate() {
        User user = new User("user2", "new User2", "tequila", "2128509", null, "T", "NYC");
        ResponseEntity<String> textResponse = template
                .postForEntity("http://localhost:" + port + "/users", user, String.class);
        assertTrue(textResponse.getStatusCode().is4xxClientError());
        System.out.println("Выполнен тест попытки создания дубликата пользователя");
    }

    @Test
    @DisplayName("Test get subscription by id")
    public void testGetSubscriptionById() {
        ResponseEntity<SubscriptionDto> response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/subscriptions/1", SubscriptionDto.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("user1", response.getBody().getSubscriber());
        System.out.println("Выполнен тест чтения подписки");
    }

    @Test
    @DisplayName("Test get subscription 404")
    public void testGetSubscriptionById404() {
        ResponseEntity<SubscriptionDto> response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/subscriptions/222", SubscriptionDto.class);
        assertEquals(404, response.getStatusCode().value());
        System.out.println("Выполнен тест неуспешного чтения подписки - запись не найдена");
    }

    @Test
    @DisplayName("Test create subscription")
    public void testSaveSubscription() {
        Subscription subscription = new Subscription(3,"user2", "user1");
        ResponseEntity<String> textResponse = template
                .postForEntity("http://localhost:" + port + "/subscriptions", subscription, String.class);
        assertEquals("Подписка пользователя user2 на пользователя user1 добавлена", textResponse.getBody());
        ResponseEntity<SubscriptionDto> response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/subscriptions/3", SubscriptionDto.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("user2", response.getBody().getSubscriber());
        System.out.println("Выполнен тест создания подписки");
    }

    @Test
    @DisplayName("Test create subscription with same users")
    public void testSaveSubscriptionSame() {
        Subscription subscription = new Subscription(4, "user1", "user1");
        ResponseEntity<String> textResponse = template
                .postForEntity("http://localhost:" + port + "/subscriptions", subscription, String.class);
        assertTrue(textResponse.getStatusCode().is4xxClientError());
        System.out.println("Выполнен тест попытки создания подписки на себя");
    }

    @Test
    @DisplayName("Test delete subscription")
    public void testDeleteSubscription() {
        ResponseEntity<SubscriptionDto> response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/subscriptions/2", SubscriptionDto.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        ResponseEntity<Void> resp = template.exchange("http://localhost:" + port + "/subscriptions/2", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/subscriptions/2", SubscriptionDto.class);
        assertTrue(response.getStatusCode().is4xxClientError());
        System.out.println("Выполнен тест удаления подписки");
    }

    @Test
    @DisplayName("Test delete subscription 404")
    public void testDeleteSubscription404() {
        ResponseEntity<Void> resp = template.exchange("http://localhost:" + port + "/subscriptions/222", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertEquals(404, resp.getStatusCode().value());
        System.out.println("Выполнен тест неуспешной попытки удаления подписки - запись не найдена");
    }

    @Test
    @DisplayName("Test update subscription")
    public void testUpdateSubscription() {
        Subscription subscription = new Subscription(1, "user1", "user5");
        ResponseEntity<Void> resp = template.exchange("http://localhost:" + port + "/subscriptions/1", HttpMethod.PUT, new HttpEntity<>(subscription), Void.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        System.out.println("Выполнен тест обновления подписки");
    }

    @Test
    @DisplayName("Test update subscription 404")
    public void testUpdateSubscription404() {
        Subscription subscription = new Subscription(222, "user1", "user5");
        ResponseEntity<Void> resp = template.exchange("http://localhost:" + port + "/subscriptions/222", HttpMethod.PUT, new HttpEntity<>(subscription), Void.class);
        assertEquals(404, resp.getStatusCode().value());
        System.out.println("Выполнен тест неуспешной обновления подписки");
    }

    @Test
    @DisplayName("Test update user 404")
    public void testUpdateUser404() {
        User user = new User("user222", "User1", "none", "2128506", null, "M", "Tashkent");
        ResponseEntity<Void> resp = template.exchange("http://localhost:" + port + "/users/user222", HttpMethod.PUT, new HttpEntity<>(user), Void.class);
        assertEquals(404, resp.getStatusCode().value());
        System.out.println("Выполнен тест неуспешной попытки обновления пользователя - запись не найдена");
    }

    @Test
    @DisplayName("Test update user")
    public void testUpdateUser() {
        User user = new User("user1", "User1", "none", "2128506", null, "M", "Tashkent");
        ResponseEntity<Void> resp = template.exchange("http://localhost:" + port + "/users/user1", HttpMethod.PUT, new HttpEntity<>(user), Void.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        System.out.println("Выполнен тест обновления пользователя");
    }

    @Test
    @DisplayName("Test delete user")
    public void testDeleteUser() {
        ResponseEntity<UserDto> response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/users/user6", UserDto.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(0, response.getBody().getDeleted());
        ResponseEntity<Void> resp = template.exchange("http://localhost:" + port + "/users/user6", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/users/user6", UserDto.class);
        assertEquals(404, response.getStatusCode().value());
        System.out.println("Выполнен тест удаления подписки");
    }

    @Test
    @DisplayName("Test delete user 404")
    public void testDeleteUser404() {
        ResponseEntity<Void> resp = template.exchange("http://localhost:" + port + "/users/user222", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertEquals(404, resp.getStatusCode().value());
        System.out.println("Выполнен тест неуспешной попытки удаления пользователя - запись не найдена");
    }

}
