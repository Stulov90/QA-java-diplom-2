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

public class CreateOrderTest {
    String accessToken;

    @Before
    public void setUp() {
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
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthUserAndIngredientsTest() {
        Response ingredientResponse = OrderSteps.getIngredients();
        List<String> ingredients = ingredientResponse.then().log().all().extract().path("data._id");
        Order order = new Order(ingredients.subList(0, 2));
        Response orderResponse = OrderSteps.createOrderWithAuthUser(order, accessToken);
        orderResponse.then().log().all().assertThat().statusCode(200)
                .and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    public void createOrderWithAuthUserAndWithoutIngredientsTest() {
        Order order = new Order(null);
        Response response = OrderSteps.createOrderWithAuthUser(order, accessToken);
        response.then().log().all().assertThat().statusCode(400)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацей и с неверным хэшем ингредиента")
    public void createOrderWithAuthUserAndWrongIngredientsTest() {
        Response ingredientResponse = OrderSteps.getIngredients();
        List<String> ingredients = ingredientResponse.then().log().all().extract().path("data._id");
        String wrongIngredient = ingredients.get(0) + "q";
        Order order = new Order(List.of(ingredients.get(1), wrongIngredient));
        Response orderResponse = OrderSteps.createOrderWithAuthUser(order, accessToken);
        orderResponse.then().log().all().assertThat().statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с ингредиентами")
    public void createOrderWithoutAuthUserAndWithIngredientsTest() {
        Response ingredientResponse = OrderSteps.getIngredients();
        List<String> ingredients = ingredientResponse.then().log().all().extract().path("data._id");
        Order order = new Order(ingredients.subList(0, 2));
        Response orderResponse = OrderSteps.createOrderWithoutAuthUser(order);
        orderResponse.then().log().all().assertThat().statusCode(200)
                .and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    public void createOrderWithoutAuthUserAndWithoutIngredientsTest() {
        Order order = new Order(null);
        Response response = OrderSteps.createOrderWithoutAuthUser(order);
        response.then().log().all().assertThat().statusCode(400)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с неверным хэшем ингредиента")
    public void createOrderWithoutAuthUserAndWrongIngredientsTest() {
        Response ingredientResponse = OrderSteps.getIngredients();
        List<String> ingredients = ingredientResponse.then().log().all().extract().path("data._id");
        String wrongIngredient = ingredients.get(0) + "q";
        Order order = new Order(List.of(ingredients.get(1), wrongIngredient));
        Response orderResponse = OrderSteps.createOrderWithoutAuthUser(order);
        orderResponse.then().log().all().assertThat().statusCode(500);
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }
}
