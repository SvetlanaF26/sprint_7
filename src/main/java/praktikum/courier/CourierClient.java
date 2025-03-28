package praktikum.courier;


import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.Client;


import java.util.Map;

public class CourierClient extends Client {

    private static final String COURIER = "courier";

    @Step("Логин курьера")
    public ValidatableResponse logIn(Credentials dataCourier) {
        return spec()
                .body(dataCourier)
                .when()
                .post(COURIER + "/login")
                .then().log().all();
    }

    @Step("Создать курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return spec()
                .body(courier)
                .when()
                .post(COURIER)
                .then().log().all();
    }

    @Step("Удалить курьера")
    public ValidatableResponse delete(int id) {
        return spec()
                .body(Map.of("id", id))
                .when()
                .post(COURIER + "/" + id)
                .then().log().all();
    }
}