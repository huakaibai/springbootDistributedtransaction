package com.zhibinwang.dt.model;

import java.math.BigDecimal;

public class TOrder {
    private String orderId;

    private BigDecimal amount;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}