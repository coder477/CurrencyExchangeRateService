package com.bk.exchange.service;

import com.bk.exchange.exception.ExternalApiException;
import com.bk.exchange.models.ExchangeRateHistory;
import com.bk.exchange.models.ExchangeRateHistoryKey;
import com.bk.exchange.repository.ExchangeHistoryDao;
import com.bk.exchange.response.CurrentExchangeRate;
import com.bk.exchange.response.ExchangeRateHistoryResponse;
import com.bk.exchange.response.Trend;
import com.bk.exchange.utils.Utils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class ExchangeService {

    private static final Logger log = getLogger(ExchangeService.class);

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ExchangeRateApiWrapper exchangeRateApiWrapper;

    @Autowired
    public ExchangeHistoryDao exchangeHistoryDao;

    public CurrentExchangeRate getExchangeRate(Date date, String baseCurrency, String targetCurrency)
            throws ParseException {

        Date endDate = getBaseDate(date);
        Date startDate = getStartDate(dateFormat.format(endDate.getTime()));

        JSONObject response = exchangeRateApiWrapper.getExchangeBetween(dateFormat.format(startDate.getTime()),
                dateFormat.format(endDate.getTime()), baseCurrency,
                targetCurrency);

        double rate =
                response.getJSONObject("rates").getJSONObject(dateFormat.format(startDate.getTime())).getDouble(targetCurrency);
        double averageRate = getAverageRate(response.getJSONObject("rates"), targetCurrency);
        Trend rateTrend = getExchangeRateTrend(response.getJSONObject("rates"), targetCurrency);

        CurrentExchangeRate currentRate = new CurrentExchangeRate();
        currentRate.setAverageRate(averageRate);
        currentRate.setExchangeRate(rate);
        currentRate.setExchangeRateTrend(rateTrend);

        ExchangeRateHistory history = new ExchangeRateHistory();
        ExchangeRateHistoryKey key = new ExchangeRateHistoryKey();
        key.setBaseCurrency(baseCurrency);
        key.setTargetCurrency(targetCurrency);
        key.setDate(endDate);
        history.setExchangeRate(rate);
        history.setExchangeTrend(rateTrend);
        history.setCumulativeExchangeRateAverage(averageRate);
        history.setKey(key);

        exchangeHistoryDao.save(history); // saving the successful requests to DB as history data

        return currentRate;
    }

    public List<ExchangeRateHistoryResponse> getDailyExchangeRateHistory(Integer year, Integer month, Integer day) {
        return exchangeHistoryDao.findExchangeRateHistoriesByYearMonthandDate(year, month, day).stream()
                .map(Utils::convert).collect(Collectors.toList());
    }

    public List<ExchangeRateHistoryResponse> getMonthlyExchangeRateHistory(Integer year, Integer month) {
        return exchangeHistoryDao.findExchangeRateHistoriesByYearMonth(year, month).stream().map(Utils::convert)
                .collect(Collectors.toList());
    }

    /**
     * gets the exchange rate trend
     */
    private Trend getExchangeRateTrend(JSONObject obj, String targetCurrency) {

        List<Date> lastFiveDays = new ArrayList<>();
        try {
            for (String key : obj.keySet()) {
                lastFiveDays.add(dateFormat.parse(key));
            }
            Collections.sort(lastFiveDays);
        } catch (ParseException ex) {
            throw new ExternalApiException("Response date not in right format", ex.getMessage());
        }

        List<Double> rates = new ArrayList<>();

        for (Date key : lastFiveDays) {
            rates.add(obj.getJSONObject(dateFormat.format(key.getTime())).getDouble(targetCurrency));
        }
        int ascending = 0;
        int descending = 0;
        for (int i = 0; i < rates.size() - 1; i++) {
            if (Double.compare(rates.get(i), rates.get(i + 1)) == -1) {
                ascending = 1;
            } else if (Double.compare(rates.get(i), rates.get(i + 1)) == 1) {
                descending = 1;
            }
        }

        if (ascending == 0 && descending == 0) {
            return Trend.CONSTANT;
        }
        if (ascending == 1 && descending == 1) {
            return Trend.UNDEFINED;
        }
        if (ascending == 1 && descending == 0) {
            return Trend.ASCENDING;
        }
        return Trend.DESCENDING;
    }

    private double getAverageRate(JSONObject obj, String targetCurrency) {
        return obj.keySet().stream().mapToDouble(x -> obj.getJSONObject(x).getDouble(targetCurrency)).average()
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * gets the 5th day back from today excluding saturdays and sundays
     */

    private Date getStartDate(String date) throws ParseException {

        Date baseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        Calendar baseDateCalculated = Calendar.getInstance();
        baseDateCalculated.setTime(baseDate);
        for (int i = 0; i < 4; i++) {
            baseDateCalculated.add(Calendar.DATE, -1);
            if (baseDateCalculated.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                baseDateCalculated.add(Calendar.DATE, -2);
            }
        }
        return baseDateCalculated.getTime();
    }

    /**
     * get the date to start finding transaction rate while handling Saturdays and Sundays
     */

    private Date getBaseDate(Date baseDate) {

        Calendar baseDateCalculated = Calendar.getInstance();
        baseDateCalculated.setTime(baseDate);
        if (baseDateCalculated.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            baseDateCalculated.add(Calendar.DATE, -2);
        }
        if (baseDateCalculated.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            baseDateCalculated.add(Calendar.DATE, -1);
        }
        return baseDateCalculated.getTime();
    }
}
