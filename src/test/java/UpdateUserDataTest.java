import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UpdateUserDataTest {
    User user;
    String accessToken;

    @Before
    public void setUp() {
        user = UserSteps.createUserWithRandomData();
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
    @DisplayName("Изменение данных авторизованного пользователя в поле name")
    public void updateFieldNameWithAuthTest() {
        User updateUser = new User(user.getEmail(), user.getPassword(), user.getName());
        updateUser.setName("Johnny Bravo");
        Response response = UserSteps.updateUserDataWithAccessToken(updateUser, accessToken);
        response.then().log().all().assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(updateUser.getName()));
    }

    @Test
    @DisplayName("Изменение данных авторизованного пользователя в поле email")
    public void updateFieldEmailWithAuthTest() {
        User updateUser = new User(user.getEmail(), user.getPassword(), user.getName());
        updateUser.setEmail("tyrionlannister@gmail.com");
        Response response = UserSteps.updateUserDataWithAccessToken(updateUser, accessToken);
        response.then().log().all().assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("user.email", equalTo(updateUser.getEmail()))
                .and().body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Изменение данных авторизованного пользователя в поле password")
    public void updateFieldPasswordWithAuthTest() {
        User updateUser = new User(user.getEmail(), user.getPassword(), user.getName());
        updateUser.setPassword("qwerty7890");
        Response response = UserSteps.updateUserDataWithAccessToken(updateUser, accessToken);
        response.then().log().all().assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("user.email", equalTo(updateUser.getEmail()))
                .and().body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Изменение данных неавторизованного пользователя в поле name")
    public void updateFieldNameWithoutAuthTest() {
        User updateUser = new User(user.getEmail(), user.getPassword(), user.getName());
        updateUser.setName("Johnny Bravo");
        Response response = UserSteps.updateUserDataWithoutAccessToken(updateUser);
        response.then().log().all().assertThat().statusCode(401)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение данных неавторизованного пользователя в поле email")
    public void updateFieldEmailWithoutAuthTest() {
        User updateUser = new User(user.getEmail(), user.getPassword(), user.getName());
        updateUser.setEmail("tyrionlannister@gmail.com");
        Response response = UserSteps.updateUserDataWithoutAccessToken(updateUser);
        response.then().log().all().assertThat().statusCode(401)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение данных неавторизованного пользователя в поле password")
    public void updateFieldPasswordWithoutAuthTest() {
        User updateUser = new User(user.getEmail(), user.getPassword(), user.getName());
        updateUser.setPassword("qwerty7890");
        Response response = UserSteps.updateUserDataWithoutAccessToken(updateUser);
        response.then().log().all().assertThat().statusCode(401)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }
}
