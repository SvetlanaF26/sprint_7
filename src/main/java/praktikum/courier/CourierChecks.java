package praktikum.courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;
import java.util.Map;


import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.junit.Assert.*;

public class CourierChecks {

    @Step("Успешное создание курьера")
    public void created(ValidatableResponse createResponse) {
        boolean created = createResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .extract()
                .path("ok");
        assertTrue(created);
    }

    @Step("Не получилось создать курьера")
    public void checkFailed(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .body().as(Map.class);

        assertEquals("Недостаточно данных для создания учетной записи", body.get("message"));

    }

    @Step("Успешный логин курьера")
    public int loginSuccess(ValidatableResponse loginResponse) {
        int id = loginResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract()
                .path("id");
        assertNotEquals(0, id);
        return id;
    }

    @Step("Попытка создать курьера с уже существующим логином")
    public void checkDuplicateLoginFailed(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CONFLICT)
                .extract()
                .body().as(Map.class);

        assertEquals("Этот логин уже используется. Попробуйте другой.", body.get("message"));
    }

    @Step("Попытка авторизации курьера без логина")
    public void checkWithoutLoginFailed(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .body().as(Map.class);

        assertEquals("Недостаточно данных для входа", body.get("message"));

    }

    @Step("Попытка авторизации курьера под несуществующим пользователем")
    public void checkAuthorizeNonexistentCourier(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .extract()
                .body().as(Map.class);

        assertEquals("Учетная запись не найдена", body.get("message"));
    }

    @Step("Попытка авторизации курьера с неправильным паролем")
    public void checkAuthorizeCourierWithIncorrectPassword(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .extract()
                .body().as(Map.class);

        assertEquals("Учетная запись не найдена", body.get("message"));
    }


}
