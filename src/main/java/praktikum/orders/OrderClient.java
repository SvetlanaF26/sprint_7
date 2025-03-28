package praktikum.orders;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.Client;


public class OrderClient extends Client {
    private static final String ORDER_ENDPOINT = "orders";

    @Step("Создать заказ")
    public ValidatableResponse createOrder(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Отменить заказ")
    public void cancelTheOrder(int trackOrder) {
        spec()
                .queryParam("track", trackOrder)
                .when()
                .put(ORDER_ENDPOINT + "/cancel")
                .then().log().all();
    }

    @Step("Получить список заказов")
    public ValidatableResponse getOrderList() {
        return spec()
                .when()
                .get(ORDER_ENDPOINT)
                .then().log().all();

    }
}
