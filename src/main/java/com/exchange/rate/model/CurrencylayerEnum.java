package com.exchange.rate.model;

public enum CurrencylayerEnum {

    SUCCESS("success"),
    TERMS("terms"),
    PRIVACY("privacy"),
    TIMESTAMP("timestamp"),
    SOURCE("source"),
    QUOTES("quotes");

    private String value;

    CurrencylayerEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return String.valueOf(this.value);
    }

}
