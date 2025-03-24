package praktikum.courier;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class CourierTest {
    private final CourierClient client = new CourierClient();
    private final CourierChecks check = new CourierChecks();
    private int courierId;

    @Test
    @DisplayName("Успешное создание курьера")
    public void courier() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.created(createResponse);
        saveCourierId(courier);
        assertNotEquals(0, courierId);
    }


    @Test
    @DisplayName("Негативная проверка, курьер без пароля")
    public void cannotCreateWithoutPassword() {
        var courier = Courier.randomWithoutPassword();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.checkFailed(createResponse);
    }


    @Test
    @DisplayName("Ошибка при создании курьера с уже существующим логином")
    public void cannotCreateCourierWithExistingLogin() {
        var courier = Courier.random();
        ValidatableResponse successResponse = client.createCourier(courier);
        check.created(successResponse);
        saveCourierId(courier);

        ValidatableResponse errorResponse = client.createCourier(courier);
        check.checkDuplicateLoginFailed(errorResponse);

    }

    @Test
    @DisplayName("Ошибка при авторизации курьера без логина")
    public void cannotAuthorizeCourierWithoutLogin() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.created(createResponse);
        saveCourierId(courier);
        var credentialWithoutLogin = new Credentials(null, courier.getPassword());
        ValidatableResponse loginErrorResponse = client.logIn(credentialWithoutLogin);
        check.checkWithoutLoginFailed(loginErrorResponse);
    }

    @Test
    @DisplayName("Ошибка при авторизации не существующего курьера")
    public void cannotAuthorizeNonexistentCourier() {
        var nonexistentCourier = new Credentials("Noname", "pas123");
        ValidatableResponse loginErrorResponse = client.logIn(nonexistentCourier);
        check.checkAuthorizeNonexistentCourier(loginErrorResponse);
    }

    @Test
    @DisplayName("Ошибка при авторизации курьера с неправильным паролем")
    public void cannotAuthorizeCourierWithIncorrectPassword() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.created(createResponse);
        saveCourierId(courier);
        var credentialWithIncorrectPassword = new Credentials(courier.getLogin(), "0000");
        ValidatableResponse loginErrorResponse = client.logIn(credentialWithIncorrectPassword);
        check.checkAuthorizeCourierWithIncorrectPassword(loginErrorResponse);
    }


    @After
    public void deleteCourier() {
        if (courierId > 0) {
            client.delete(courierId);
            courierId = 0;
        }
    }

    private void saveCourierId(Courier courier) {
        var dataCourier = Credentials.fromCourier(courier);
        ValidatableResponse loginResponse = client.logIn(dataCourier);
        courierId = check.loginSuccess(loginResponse);
    }
}