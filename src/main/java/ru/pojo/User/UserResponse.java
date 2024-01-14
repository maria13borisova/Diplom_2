package ru.pojo.User;

public class UserResponse {
    private boolean success;
    private UserLogin user;
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

}