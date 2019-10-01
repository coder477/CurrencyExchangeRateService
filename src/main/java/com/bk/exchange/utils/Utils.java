package com.bk.exchange.utils;

import com.bk.exchange.exception.BadRequestException;
import com.bk.exchange.models.ExchangeRateHistory;
import com.bk.exchange.response.ExchangeRateHistoryResponse;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static String specifiedLimitDate = "2000-01-01";

    public static Date parseDate(String date) {
        dateFormat.setLenient(false);
        Date d = null;
        try {
            d = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Please enter date in valid format yyyy-MM-dd ");
        }
        return d;
    }

    public static void validateDate(String date) {
        Date d = parseDate(date);
        Calendar baseDateCal = Calendar.getInstance();
        baseDateCal.setTime(d);
        if (ChronoUnit.DAYS.between(baseDateCal.toInstant(), Calendar.getInstance().toInstant()) < 1) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Please enter date less than yesterday");
        }
        Date dSpecified = parseDate(specifiedLimitDate);
        if (dSpecified.after(d)) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Please enter date later than " + specifiedLimitDate);
        }
    }

    public static void validateMonth(String MM) {
        try {
            int x = Integer.parseInt(MM);
            if (x < 1 || x > 12) {
                throw new BadRequestException(HttpStatus.BAD_REQUEST, "Please input a valid month");
            }
        } catch (NumberFormatException ex) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Please input a valid month");
        }
    }

    public static void validateDay(String dd) {
        try {
            int x = Integer.parseInt(dd);
            if (x < 1 || x > 31) {
                throw new BadRequestException(HttpStatus.BAD_REQUEST, "Please input a valid day");
            }
        } catch (NumberFormatException ex) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Please input a valid day");
        }
    }

    public static void validateYear(String yyyy) {
        try {
            int x = Integer.parseInt(yyyy);
            if (x < 1) {
                throw new BadRequestException(HttpStatus.BAD_REQUEST, "Please input a valid year");
            }
        } catch (NumberFormatException ex) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Please input a valid year");
        }
    }

    public static ExchangeRateHistoryResponse convert(ExchangeRateHistory exchangeRateHistory) {
        return new ExchangeRateHistoryResponse(
                exchangeRateHistory.getKey().getBaseCurrency(),
                exchangeRateHistory.getKey().getTargetCurrency(),
                String.valueOf(exchangeRateHistory.getExchangeRate()),
                String.valueOf(exchangeRateHistory.getCumulativeExchangeRateAverage()),
                exchangeRateHistory.getExchangeTrend(),
                dateFormat.format(exchangeRateHistory.getKey().getDate()));
    }
}
