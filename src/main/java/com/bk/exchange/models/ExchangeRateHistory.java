package com.bk.exchange.models;

import com.bk.exchange.response.Trend;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "exchange_history", schema = "bk_exchange_demo")
public class ExchangeRateHistory {

    @Column(name = "exchange_rate")
    private double exchangeRate;

    @Column(name = "cumulative_exchange_rate_average")
    private double cumulativeExchangeRateAverage;

    @Column(name = "exchange_trend")
    @Enumerated(EnumType.STRING)
    private Trend exchangeTrend;

    @EmbeddedId
    @JsonProperty("query")
    private ExchangeRateHistoryKey key;

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public double getCumulativeExchangeRateAverage() {
        return cumulativeExchangeRateAverage;
    }

    public void setCumulativeExchangeRateAverage(double cumulativeExchangeRateAverage) {
        this.cumulativeExchangeRateAverage = cumulativeExchangeRateAverage;
    }

    public Trend getExchangeTrend() {
        return exchangeTrend;
    }

    public void setExchangeTrend(Trend exchangeTrend) {
        this.exchangeTrend = exchangeTrend;
    }

    public ExchangeRateHistoryKey getKey() {
        return key;
    }

    public void setKey(ExchangeRateHistoryKey key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangeRateHistory history = (ExchangeRateHistory) o;

        if (Double.compare(history.exchangeRate, exchangeRate) != 0) return false;
        if (Double.compare(history.cumulativeExchangeRateAverage, cumulativeExchangeRateAverage) != 0) return false;
        if (exchangeTrend != null ? !exchangeTrend.equals(history.exchangeTrend) : history.exchangeTrend != null)
            return false;
        return key != null ? key.equals(history.key) : history.key == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(exchangeRate);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(cumulativeExchangeRateAverage);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (exchangeTrend != null ? exchangeTrend.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }
}

