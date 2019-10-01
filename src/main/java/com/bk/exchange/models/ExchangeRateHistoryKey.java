package com.bk.exchange.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ExchangeRateHistoryKey implements Serializable {

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "base_currency")
    private String baseCurrency;

    @Column(name = "target_currency")
    private String targetCurrency;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangeRateHistoryKey that = (ExchangeRateHistoryKey) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (baseCurrency != null ? !baseCurrency.equals(that.baseCurrency) : that.baseCurrency != null) return false;
        return targetCurrency != null ? targetCurrency.equals(that.targetCurrency) : that.targetCurrency == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (baseCurrency != null ? baseCurrency.hashCode() : 0);
        result = 31 * result + (targetCurrency != null ? targetCurrency.hashCode() : 0);
        return result;
    }
}
