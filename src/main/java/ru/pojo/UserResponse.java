package ru.pojo;

public class UserResponse {
    private boolean success;
    private UserLogin user;
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

}