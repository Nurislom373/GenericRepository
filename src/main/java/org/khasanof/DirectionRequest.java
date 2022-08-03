package org.khasanof;

import org.khasanof.enums.Order;

public class DirectionRequest {
    private int page;
    private int size;
    private Order orderKey;
    private String orderValue;

    private String[] orderValues;

    public DirectionRequest() {
        this.size = 10;
    }

    public DirectionRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public DirectionRequest(int page, int size, Order orderKey, String orderValue) {
        this.page = page;
        this.size = size;
        this.orderKey = orderKey;
        this.orderValue = orderValue;
    }

    private DirectionRequest(int page, int size, Order orderKey, String... orderValues) {
        this.page = page;
        this.size = size;
        this.orderKey = orderKey;
        this.orderValues = orderValues;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Order getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(Order orderKey) {
        this.orderKey = orderKey;
    }

    public String getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(String orderValue) {
        this.orderValue = orderValue;
    }

    public String[] getOrderValues() {
        return orderValues;
    }

    public void setOrderValues(String[] orderValues) {
        this.orderValues = orderValues;
    }

}
