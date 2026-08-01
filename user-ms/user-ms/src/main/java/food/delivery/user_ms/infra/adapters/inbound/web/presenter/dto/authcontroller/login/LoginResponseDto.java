package food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login;

import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get.UserResponseDto;

public class LoginResponseDto {

    private String token;
    private UserResponseDto user;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String token, UserResponseDto user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public void setUser(UserResponseDto user) {
        this.user = user;
    }
}
