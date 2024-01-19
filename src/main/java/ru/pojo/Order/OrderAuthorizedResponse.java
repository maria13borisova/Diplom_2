package ru.pojo.Order;

public class OrderAuthorizedResponse {
    private boolean success;
    private String name;
    private OrderAuthorizedData order;

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

    public OrderAuthorizedData getOrder() {
        return order;
    }

    public void setOrder(OrderAuthorizedData order) {
        this.order = order;
    }
}
