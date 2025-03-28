package praktikum.orders;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotEquals;

public class OrderChecks {
    @Step("Успешное создание заказа")
    public int createdOrder(ValidatableResponse createResponse) {
        int orderTrack = createResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .extract()
                .path("track");
        assertNotEquals(0, orderTrack);
        return orderTrack;

    }

    @Step("Успешное получение списка заказов курьера")
    public void checkListOfOrders(ValidatableResponse response) {
        var extracted = response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract()
                .response();

        JsonPath jsonPath = extracted.jsonPath();
        List<Map<String, Object>> orders = jsonPath.getList("orders");
        assertThat("Список заказов не должен быть пустым", orders, not(empty()));
    }
}
