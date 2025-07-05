import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest {
    private User user;
    String accessToken;

    @Before
    public void setUp() {
        user = UserSteps.createUserWithRandomData();
        Response firstResponse = UserSteps.createUser(user);
        accessToken = firstResponse.then().extract().path("accessToken");
        firstResponse.then().log().all().assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("accessToken", notNullValue())
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginExistUserTest() {
        User loginUser = new User(user.getEmail(), user.getPassword(), null);
        Response response = UserSteps.loginUser(loginUser);
        response.then().log().all().assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("accessToken", notNullValue())
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Логин пользователя с неверным полем login")
    public void loginUserWithWrongEmailTest() {
        User loginUser = new User(user.getEmail(), user.getPassword(), null);
        loginUser.setEmail("tyrionlannister@gmail.com");
        Response response = UserSteps.loginUser(loginUser);
        response.then().log().all().assertThat().statusCode(401)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин пользователя с неверным полем password")
    public void loginUserWithWrongPasswordTest() {
        User loginUser = new User(user.getEmail(), user.getPassword(), null);
        loginUser.setPassword("qwertyqaz1234");
        Response response = UserSteps.loginUser(loginUser);
        response.then().log().all().assertThat().statusCode(401)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }
}
