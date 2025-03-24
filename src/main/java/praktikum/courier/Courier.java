package praktikum.courier;

import java.util.concurrent.ThreadLocalRandom;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Courier() {

    }

    // курьер со всеми обязательными полями
    public static Courier random() {
        int suffix = ThreadLocalRandom.current().nextInt(100, 100_000);
        return new Courier("Courier" + suffix, "C0urier!Pass99", "Arrow");
    }

    // курьер без "пароля"
    public static Courier randomWithoutPassword() {
        int suffix = ThreadLocalRandom.current().nextInt(100, 100_000);
        return new Courier("Courier" + suffix, null, "Arrow");
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
