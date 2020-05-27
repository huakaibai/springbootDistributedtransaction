package com.zhibinwang.dt.model;

public class TOrder {
    private String odrerId;

    private String amount;

    public String getOdrerId() {
        return odrerId;
    }

    public void setOdrerId(String odrerId) {
        this.odrerId = odrerId == null ? null : odrerId.trim();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }
}