package order;

import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static constants.Client.*;
import static io.qameta.allure.model.Parameter.Mode.HIDDEN;

public class OrderSteps {

    @Step("Получение списка всех ингредиентов")
    public static Response getIngredients() {
        return spec()
                .when()
                .get(LIST_OF_INGREDIENTS);
    }

    @Step("Создание заказа с авторизацией пользователя")
    public static Response createOrderWithAuthUser(Order order, @Param(mode = HIDDEN) String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(CREATE_AND_GET_ORDER);
    }

    @Step("Создание заказа без авторизации пользователя")
    public static Response createOrderWithoutAuthUser(Order order) {
        return spec()
                .body(order)
                .when()
                .post(CREATE_AND_GET_ORDER);
    }

    @Step("Получение заказов конкретного пользователя с авторизацией")
    public static Response getOrdersWithAuthUser(@Param(mode = HIDDEN) String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .get(CREATE_AND_GET_ORDER);
    }

    @Step("Получение заказов конкретного пользователя без авторизации")
    public static Response getOrdersWithoutAuthUser() {
        return spec()
                .when()
                .get(CREATE_AND_GET_ORDER);
    }
}
