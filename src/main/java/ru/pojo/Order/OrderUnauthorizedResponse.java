package ru.pojo.Order;

public class OrderUnauthorizedResponse {
    private boolean success;
    private String name;
    private OrderUnauthorized order;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderUnauthorized getOrder() {
        return order;
    }

    public void setOrder(OrderUnauthorized order) {
        this.order = order;
    }
}
