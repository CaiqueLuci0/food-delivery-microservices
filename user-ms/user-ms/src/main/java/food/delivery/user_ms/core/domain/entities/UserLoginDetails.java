package food.delivery.user_ms.core.domain.entities;

public class UserLoginDetails {

    private final String token;
    private final User user;

    public UserLoginDetails(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
