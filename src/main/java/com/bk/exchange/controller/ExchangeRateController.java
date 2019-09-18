package com.bk.exchange.controller;

import com.bk.exchange.exception.BadRequestException;
import com.bk.exchange.exception.ExternalApiException;
import com.bk.exchange.response.CurrentExchangeRate;
import com.bk.exchange.response.ErrorReponse;
import com.bk.exchange.response.ExchangeRateHistoryResponse;
import com.bk.exchange.service.ExchangeService;
import com.bk.exchange.utils.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/api/exchange-rate")
public class ExchangeRateController {

	@Autowired
	private ExchangeService exchangeService;

	private static final Logger log = getLogger(ExchangeService.class);
	
	/**
     * Service to find exchange rate between two currencies on a given date
     */

	@RequestMapping(method = RequestMethod.GET, value = "/{date}/{baseCurrency}/{targetCurrency}")
	public ResponseEntity exchangeRate(@PathVariable String date, @PathVariable String baseCurrency,
			@PathVariable String targetCurrency) throws ParseException {
		Utils.validateDate(date);
		CurrentExchangeRate currentExchangeRate = exchangeService.getExchangeRate(Utils.parseDate(date),
				baseCurrency,
				targetCurrency);
		return ResponseEntity.ok(currentExchangeRate);
	}
	
	/**
     * Service to find exchange rate history on a given date
     */

	@RequestMapping(method = RequestMethod.GET, value = "/history/daily/{year}/{month}/{day}")
	public ResponseEntity getDailyExchangeRateHistory(@PathVariable String year, @PathVariable String month,
													  @PathVariable String day) throws ParseException {
		Utils.validateDay(day);
		Utils.validateYear(year);
		Utils.validateMonth(month);

		List<ExchangeRateHistoryResponse> exchangeRateHistoryObject =
				exchangeService.getDailyExchangeRateHistory(
						Integer.parseInt(year),
						Integer.parseInt(month),
						Integer.parseInt(day)
				);
		return ResponseEntity.ok(exchangeRateHistoryObject);
	}
	
	/**
     * Service to find exchange rate history on a given month of the year
     */

	@RequestMapping(method = RequestMethod.GET, value = "/history/monthly/{yyyy}/{MM}")
	public ResponseEntity getMothlyExchangeRateHistory(@PathVariable String yyyy, @PathVariable String MM) {

		Utils.validateYear(yyyy);
		Utils.validateMonth(MM);

		List<ExchangeRateHistoryResponse> exchangeRateHistoryObject=
				exchangeService.getMonthlyExchangeRateHistory(Integer.parseInt(yyyy), Integer.parseInt(MM));
		return ResponseEntity.ok(exchangeRateHistoryObject);
	}

	/**
	 *
	 * Exception handlers for this controller are added below.
	 */

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity handleBadRequestException(BadRequestException exception) {
		log.error("Exception while processing request", exception);
		return ResponseEntity
				.badRequest()
				.body(new ErrorReponse(exception.getValue().value(),
						"Please verify the given request."
						, exception.getMessage())
				);
	}

	@ExceptionHandler(ExternalApiException.class)
	public ResponseEntity handleExternalApiError(ExternalApiException exception) {
		log.error("Exception while processing request", exception);
		return ResponseEntity
				.badRequest()
				.body(new ErrorReponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"Somthing went Wrong.",
						exception.getValue())
				);
	}
}
