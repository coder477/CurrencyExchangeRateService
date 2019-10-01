package com.bk.exchange.response;

public class CurrentExchangeRate {

    private Double exchangeRate;

    private Double averageRate;

    private Trend exchangeRateTrend;

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(double averageRate) {
        this.averageRate = averageRate;
    }

    public Trend getExchangeRateTrend() {
        return exchangeRateTrend;
    }

    public void setExchangeRateTrend(Trend exchangeRateTrend) {
        this.exchangeRateTrend = exchangeRateTrend;
    }
}
