package ru.yandex.api;

public class BaseAPI {

    public static final String BASE_URL =  "https://stellarburgers.nomoreparties.site";

    public static final String createClientAPI = BASE_URL+"/api/auth/register";
    public static final String authClientAPI = BASE_URL+"/api/auth/login";
    public static final String updateClientAPI = BASE_URL+"/api/auth/user";
    public static final String logoutClientAPI = BASE_URL+"/api/auth/logout";
    public static final String getIngredientsAPI = BASE_URL+"/api/ingredients";
    public static final String createOrderAPI = BASE_URL+"/api/orders";
}
