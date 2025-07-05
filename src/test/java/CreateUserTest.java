import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import user.User;
import user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateUserTest {
    String accessToken;

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUserTest() {
        User user = UserSteps.createUserWithRandomData();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().extract().path("accessToken");
        response.then().log().all().assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("accessToken", notNullValue())
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createDuplicateUserTest() {
        User user = UserSteps.createUserWithRandomData();
        Response firstResponse = UserSteps.createUser(user);
        accessToken = firstResponse.then().extract().path("accessToken");
        firstResponse.then().log().all().assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("accessToken", notNullValue())
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(user.getName()));
        Response secondResponse = UserSteps.createUser(user);
        secondResponse.then().log().all().assertThat().statusCode(403)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля email")
    public void createUserWithoutEmailTest() {
        User user = UserSteps.createUserWithRandomData();
        user.setEmail(null);
        Response response = UserSteps.createUser(user);
        accessToken = response.then().extract().path("accessToken");
        response.then().log().all().assertThat().statusCode(403)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля password")
    public void createUserWithoutPasswordTest() {
        User user = UserSteps.createUserWithRandomData();
        user.setPassword(null);
        Response response = UserSteps.createUser(user);
        accessToken = response.then().extract().path("accessToken");
        response.then().log().all().assertThat().statusCode(403)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля name")
    public void createUserWithoutNameTest() {
        User user = UserSteps.createUserWithRandomData();
        user.setName(null);
        Response response = UserSteps.createUser(user);
        accessToken = response.then().extract().path("accessToken");
        response.then().log().all().assertThat().statusCode(403)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }
}
