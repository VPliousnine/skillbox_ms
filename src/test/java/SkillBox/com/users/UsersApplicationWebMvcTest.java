package SkillBox.com.users;

import SkillBox.com.users.entity.Subscription;
import SkillBox.com.users.entity.User;
import SkillBox.com.users.repository.SubscriptionRepository;
import SkillBox.com.users.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UsersApplicationWebMvcTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    ObjectMapper mapper = new ObjectMapper();

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
        userRepository.save(new User("user6", "User6", "absinthe", "212850b", null, "T", "Paris"));
        userRepository.save(new User("user7", "User7", "spirit", "212850c", null, "L", "Nowhere", 1));
        subscriptionRepository.save(new Subscription("user1", "user2"));
        subscriptionRepository.save(new Subscription("user1", "user5"));
        subscriptionRepository.save(new Subscription("user1", "user6"));
    }

    @Test
    @BeforeEach
    @DisplayName("Test get user by id")
    public void getUserById() throws Exception {
        var expected = new User("user1", "User1", "none", "2128506", null, "M", "Moscow");
        var actual = mockMvc.perform(get(urlUsers() + "/user1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonAssert.assertJsonEquals(expected, actual);
        System.out.println("Выполнен тест чтения пользователя из БД");
    }

    @Test
    @DisplayName("Test get user 404")
    public void getUserById404() throws Exception {
        mockMvc.perform(get(urlUsers() + "/user0"))
                .andExpect(status().isNotFound());
        System.out.println("Выполнен тест неуспешного чтения пользователя - запись не найдена");
    }

    @Test
    @DisplayName("Test create user")
    public void saveUser() throws Exception {
        User expectedReadResponse = new User("user3", "User3", "whiskey", "2128508", null, "T", "Wa");
        String expectedSaveResponse = "Пользователь user3 добавлен";
        String json = mapper.writeValueAsString(expectedReadResponse);
        var actualSaveResponse = mockMvc.perform(post(urlUsers())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(expectedSaveResponse, actualSaveResponse);
        System.out.println("Выполнен тест создания пользователя");

        var actualReadResponse = mockMvc.perform(get(urlUsers() + "/user3"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonAssert.assertJsonEquals(actualReadResponse, expectedReadResponse);
    }

    @Test
    @DisplayName("Test create user duplicate")
    public void createUserDuplicate() throws Exception {
        User duplicateUser = new User("user2", "new User2", "tequila", "2128509", null, "T", "NYC");
        String json = mapper.writeValueAsString(duplicateUser);
        mockMvc.perform(post(urlUsers())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity());
        System.out.println("Выполнен тест попытки создания дубликата пользователя");
    }

    @Test
    @DisplayName("Test get subscription by id")
    public void getSubscriptionById() throws Exception {
        var expected = new Subscription(1, "user1", "user2");
        var actual = mockMvc.perform(get(urlSubscriptions() + "/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonAssert.assertJsonEquals(expected, actual);
        System.out.println("Выполнен тест чтения подписки");
    }

    @Test
    @DisplayName("Test get subscription 404")
    public void getSubscriptionById404() throws Exception {
        mockMvc.perform(get(urlSubscriptions() + "/222"))
                .andExpect(status().isNotFound());
        System.out.println("Выполнен тест неуспешного чтения подписки - запись не найдена");
    }

    @Test
    @DisplayName("Test create subscription")
    public void saveSubscription() throws Exception {
        Subscription expectedReadResponse = new Subscription(4,"user2", "user1");
        String expectedSaveResponse = "Подписка пользователя user2 на пользователя user1 добавлена";
        String json = mapper.writeValueAsString(expectedReadResponse);
        var actualSaveResponse = mockMvc.perform(post(urlSubscriptions())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(expectedSaveResponse, actualSaveResponse);

        var actualReadResponse = mockMvc.perform(get(urlSubscriptions() + "/4"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonAssert.assertJsonEquals(expectedReadResponse, actualReadResponse);
    }

    @Test
    @DisplayName("Test create subscription to self")
    public void saveSubscriptionSame() throws Exception {
        Subscription subscription = new Subscription(5, "user1", "user1");
        String json = mapper.writeValueAsString(subscription);
        mockMvc.perform(post(urlSubscriptions())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity());
        System.out.println("Выполнен тест попытки создания подписки на себя");
    }

    @Test
    @DisplayName("Test delete subscription")
    public void deleteSubscription() throws Exception {
        String expectedResponse = "Подписка 2 удалена";
        mockMvc.perform(get(urlSubscriptions() + "/2"))
                .andExpect(status().isOk());

        var actualResponse = mockMvc.perform(delete(urlSubscriptions() + "/2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(expectedResponse, actualResponse);

        mockMvc.perform(get(urlSubscriptions() + "/2"))
                .andExpect(status().isNotFound());

        System.out.println("Выполнен тест удаления подписки");
    }

    @Test
    @DisplayName("Test delete subscription 404")
    public void deleteSubscription404() throws Exception {
        mockMvc.perform(delete(urlSubscriptions() + "/222"))
                .andExpect(status().isNotFound());

        System.out.println("Выполнен тест неуспешной попытки удаления подписки - запись не найдена");
    }

    @Test
    @DisplayName("Test update subscription")
    public void updateSubscription() throws Exception {
        Subscription subscription = new Subscription(3, "user1", "user5");
        String json = mapper.writeValueAsString(subscription);

        mockMvc.perform(put(urlSubscriptions() + "/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        System.out.println("Выполнен тест обновления подписки");
    }

    @Test
    @DisplayName("Test update subscription 400")
    public void updateSubscription400() throws Exception {
        Subscription subscription = new Subscription(222, "user1", "user5");
        String json = mapper.writeValueAsString(subscription);

        mockMvc.perform(put(urlSubscriptions() + "/333")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        System.out.println("Выполнен тест неверного запроса обновления подписки");
    }

    @Test
    @DisplayName("Test update subscription 404")
    public void updateSubscription404() throws Exception {
        Subscription subscription = new Subscription(222, "user1", "user5");
        String json = mapper.writeValueAsString(subscription);

        mockMvc.perform(put(urlSubscriptions() + "/222")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());

        System.out.println("Выполнен тест неуспешного обновления подписки");
    }

    @Test
    @DisplayName("Test update user 400")
    public void updateUser400() throws Exception {
        User user = new User("user222", "User1", "none", "2128506", null, "M", "Tashkent");
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(put(urlUsers() + "/user333")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        System.out.println("Выполнен тест неверного запроса обновления пользователя - запись не найдена");
    }

    @Test
    @DisplayName("Test update user 404")
    public void updateUser404() throws Exception {
        User user = new User("user222", "User1", "none", "2128506", null, "M", "Tashkent");
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(put(urlUsers() + "/user222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());

        System.out.println("Выполнен тест неуспешной попытки обновления пользователя - запись не найдена");
    }

    @Test
    @DisplayName("Test update user")
    public void updateUser() throws Exception {
        User user = new User("user1", "User1", "none", "2128506", null, "M", "Tashkent");
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(put(urlUsers() + "/user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        System.out.println("Выполнен тест обновления пользователя");
    }

    @Test
    @DisplayName("Test delete user")
    public void deleteUser() throws Exception {
        String expectedResponse = "Пользователь user6 удалён";

        mockMvc.perform(get(urlUsers() + "/user6"))
                .andExpect(status().isOk());

        var actualResponse = mockMvc.perform(delete(urlUsers() + "/user6"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(expectedResponse, actualResponse);

        mockMvc.perform(get(urlUsers() + "/user6"))
                .andExpect(status().isNotFound());

        System.out.println("Выполнен тест удаления пользователя");
    }

    @Test
    @DisplayName("Test delete user 404")
    public void deleteUser404() throws Exception {
        mockMvc.perform(get(urlUsers() + "/user222"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete(urlUsers() + "/user222"))
                .andExpect(status().isNotFound());

        System.out.println("Выполнен тест неуспешной попытки удаления пользователя - запись не найдена");
    }

    @Test
    @DisplayName("Test access deleted user")
    public void accessDeletedUser() throws Exception {
        User user = new User("user7", "User1", "none", "2128506", null, "M", "Tashkent");
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(get(urlUsers() + "/user7"))
                .andExpect(status().isNotFound());

        mockMvc.perform(put(urlUsers() + "/user7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete(urlUsers() + "/user7"))
                .andExpect(status().isNotFound());

        System.out.println("Выполнен тест доступа к удалённой записи");
    }

    String urlUsers() {
        return "http://localhost:" + port + "/users";
    }

    String urlSubscriptions() {
        return "http://localhost:" + port + "/subscriptions";
    }
}
