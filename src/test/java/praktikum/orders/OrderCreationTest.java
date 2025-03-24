package praktikum.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    private final OrderClient client = new OrderClient();
    private final OrderChecks check = new OrderChecks();
    private int trackOrder;

    private String[] color;


    public OrderCreationTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        });
    }


    @Test
    @DisplayName("Проверка создания заказа с использованием параметризации поля color")
    public void orderCreationTest() {
        var order = Order.createTestOrder();
        order.setColor(color);
        ValidatableResponse createResponse = client.createOrder(order);
        trackOrder = check.createdOrder(createResponse);
    }

    @Test
    @DisplayName("Проверка, что в теле ответа возвращается список заказов")
    public void getListOfOrdersTest() {

        ValidatableResponse createResponse = client.getOrderList();
        check.checkListOfOrders(createResponse);

    }


    @After
    public void cancelTheOrder() {
        if (trackOrder > 0) {
            client.cancelTheOrder(trackOrder);
            trackOrder = 0;
        }
    }
}
