package org.khasanof.utils;

import org.khasanof.enums.Order;

public class Sort {
    private String property;
    private String direct;
    private static final String DEFAULT_ORDER = Order.DESC.getValue();

    private Sort(String property, String direct) {
        this.property = property;
        this.direct = direct;
    }

    public static Sort of(String property) {
        Asserts.notNull(property, "property must not be null!");
        return new Sort(property, DEFAULT_ORDER);
    }

    public static Sort of(String property, Order order) {
        Asserts.notNull(property, "property must not be null!");
        Asserts.notNull(order, "order must not be null!");
        return new Sort(property, order.getValue());
    }

    public String getProperty() {
        return property;
    }

    public String getDirect() {
        return direct;
    }

}
