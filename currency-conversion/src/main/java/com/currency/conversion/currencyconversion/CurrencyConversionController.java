package com.currency.conversion.currencyconversion;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;

	@Autowired
	private  BankAccountProxy bankAccountProxy;

	//localhost:8100/currency-conversion-feign?from=EUR&to=RSD&quantity=50
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public ResponseEntity<?> getConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable double quantity, HttpServletRequest request) throws Exception {
		try {
			String email = request.getHeader("X-User-Email");
			BankAccountDto userBankAccount = bankAccountProxy.getBankAccount(email).getBody();

			BigDecimal availableAmount;
			if (from.equals("RSD")) {
				availableAmount = userBankAccount.getRSD_amount();
			} else if (from.equals("USD")) {
				availableAmount = userBankAccount.getUSD_amount();
			} else if (from.equals("EUR")) {
				availableAmount = userBankAccount.getEUR_amount();
			} else if (from.equals("CHF")) {
				availableAmount = userBankAccount.getCHF_amount();
			} else if (from.equals("GBP")) {
				availableAmount = userBankAccount.getGBP_amount();
			} else {
				throw new CustomExceptions.InvalidRequestParameterValueException("Currency not supported.");
			}

			if (availableAmount.compareTo(BigDecimal.valueOf(quantity)) >= 0) {
				return getResponseEntity(from, to, quantity, email, currencyExchangeProxy, bankAccountProxy);
			} else {
				throw new CustomExceptions.InsufficientFundsException("User doesn't have the given amount of " + from + " currency on their account. Account amount is " + availableAmount + " and the specified amount is " + quantity);
			}
		} catch (CustomExceptions.EntityDoesntExistException | CustomExceptions.InvalidRequestParameterValueException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	private static ResponseEntity<?> getResponseEntity(@PathVariable String from, @PathVariable String to, @PathVariable double quantity, String email, CurrencyExchangeProxy currencyExchangeProxy, BankAccountProxy bankAccountProxy) throws CustomExceptions.EntityDoesntExistException {
		ResponseEntity<CurrencyConversion> response = currencyExchangeProxy.getExchange(from, to);
		if (response == null) {
			throw new CustomExceptions.EntityDoesntExistException("Currency exchange data not found!");
		}
		CurrencyConversion responseBody = response.getBody();
		CurrencyConversion newConversion = new CurrencyConversion(from, to, responseBody.getConversionMultiple(), responseBody.getEnvironment() + " feign",
				quantity, responseBody.getConversionMultiple().multiply(BigDecimal.valueOf(quantity)));
		BankAccountDto updatedBalance = bankAccountProxy.updateBankAccountBalance(email, BigDecimal.valueOf(quantity), from, newConversion.getConversionTotal(), to).getBody();
		return ResponseEntity.ok().body(
				"User : " + email + System.lineSeparator() +
				"RSD amount : " + updatedBalance.getRSD_amount().toString() + System.lineSeparator() +
				"USD amount : " + updatedBalance.getUSD_amount().toString() + System.lineSeparator() +
				"GBP amount : " + updatedBalance.getGBP_amount().toString() + System.lineSeparator() +
				"CHF amount : " + updatedBalance.getCHF_amount().toString() + System.lineSeparator() +
				"EUR amount : " + updatedBalance.getEUR_amount().toString() + System.lineSeparator() +
				"User successfully converted " + quantity
				+ " of " + from + " to " + newConversion.getConversionTotal() + " of " + to);
	}
}
