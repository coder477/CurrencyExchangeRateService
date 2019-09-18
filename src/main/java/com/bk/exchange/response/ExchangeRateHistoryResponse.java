package com.bk.exchange.response;

public class ExchangeRateHistoryResponse {

    private String baseCurrency;
    private String targetCurrency;
    private String exchangeRate;
    private String cumulativeExchangeRateAverage;
    private Trend exchangeTrend;
    private String date;

    public ExchangeRateHistoryResponse(String baseCurrency, String targetCurrency, String exchangeRate,
                                   String cumulativeExchangeRateAverage, Trend exchangeTrend, String date) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
        this.cumulativeExchangeRateAverage = cumulativeExchangeRateAverage;
        this.exchangeTrend = exchangeTrend;
        this.date = date;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCumulativeExchangeRateAverage() {
        return cumulativeExchangeRateAverage;
    }

    public void setCumulativeExchangeRateAverage(String cumulativeExchangeRateAverage) {
        this.cumulativeExchangeRateAverage = cumulativeExchangeRateAverage;
    }

    public Trend getExchangeTrend() {
        return exchangeTrend;
    }

    public void setExchangeTrend(Trend exchangeTrend) {
        this.exchangeTrend = exchangeTrend;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
