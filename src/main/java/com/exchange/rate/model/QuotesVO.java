package com.exchange.rate.model;

import java.util.Map;

public class QuotesVO {

    private Map<String, Double> map;

    public QuotesVO(Map<String, Double> map) {
        this.map = map;
    }

    public Double getQuotes(String quotes) {
        return map.get(quotes);
    }
}
