package user;

import constants.RandomizerForCreateUser;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static constants.Client.*;
import static io.qameta.allure.model.Parameter.Mode.HIDDEN;

public class UserSteps {

    public static User createUserWithRandomData() {
        return new User(
                RandomizerForCreateUser.RANDOM_EMAIL,
                RandomizerForCreateUser.RANDOM_PASSWORD,
                RandomizerForCreateUser.RANDOM_NAME
        );
    }

    @Step("Создание уникального пользователя")
    public static Response createUser(User user) {
        return spec()
                .body(user)
                .when()
                .post(CREATE_USER);
    }

    @Step("Логин пользователя в системе")
    public static Response loginUser(User user) {
        return spec()
                .body(user)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Обновление данных пользователя с передачей accessToken")
    public static Response updateUserDataWithAccessToken(User user, @Param(mode=HIDDEN)String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(DATA_USER);
    }

    @Step("Обновление данных пользователя без передачи accessToken")
    public static Response updateUserDataWithoutAccessToken(User user) {
        return spec()
                .body(user)
                .when()
                .patch(DATA_USER);
    }

    @Step("Удаление пользователя")
    public static void deleteUser(@Param(mode=HIDDEN)String accessToken) {
        spec()
                .header("Authorization", accessToken)
                .when()
                .delete(DATA_USER);
    }
}
