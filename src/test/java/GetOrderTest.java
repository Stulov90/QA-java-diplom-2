import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Order;
import order.OrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserSteps;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderTest {
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
        Response ingredientResponse = OrderSteps.getIngredients();
        List<String> ingredients = ingredientResponse.then().log().all().extract().path("data._id");
        Order order = new Order(ingredients.subList(0, 2));
        Response orderResponse = OrderSteps.createOrderWithAuthUser(order, accessToken);
        orderResponse.then().log().all().assertThat().statusCode(200)
                .and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказа авторизанного пользователя")
    public void getOrderWithAuthTest() {
        Response response = OrderSteps.getOrdersWithAuthUser(accessToken);
        response.then().log().all().assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("orders", notNullValue())
                .and().body("total", notNullValue())
                .and().body("totalToday", notNullValue());
    }

    @Test
    @DisplayName("Получение заказа авторизанного пользователя")
    public void getOrderWithoutAuthTest() {
        Response response = OrderSteps.getOrdersWithoutAuthUser();
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
