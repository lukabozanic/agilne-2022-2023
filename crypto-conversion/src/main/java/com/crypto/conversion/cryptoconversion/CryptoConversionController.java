package com.crypto.conversion.cryptoconversion;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CryptoConversionController {

    @Autowired
    private CryptoExchangeProxy cryptoExchangeProxy;

    @Autowired
    private  CryptoWalletProxy cryptoWalletProxy;

    @GetMapping("/crypto-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<?> getConversionFeign(@PathVariable String from, @PathVariable String to,
                                                @PathVariable double quantity, HttpServletRequest request) throws Exception {
        try {
            String email = request.getHeader("X-User-Email");
            CryptoWalletDto userCryptoWallet = cryptoWalletProxy.getCryptoWallet(email).getBody();

            BigDecimal availableAmount;
            if (from.equals("BTC")) {
                availableAmount = userCryptoWallet.getBTC_amount();
            } else if (from.equals("ETH")) {
                availableAmount = userCryptoWallet.getETH_amount();
            } else if (from.equals("LTC")) {
                availableAmount = userCryptoWallet.getLTC_amount();
            } else if (from.equals("XRP")) {
                availableAmount = userCryptoWallet.getXRP_amount();
            } else {
                throw new CustomExceptions.InvalidRequestParameterValueException("Currency not supported.");
            }

            if (availableAmount.compareTo(BigDecimal.valueOf(quantity)) >= 0) {
                return getResponseEntity(from, to, quantity, email, cryptoExchangeProxy, cryptoWalletProxy);
            } else {
                throw new CustomExceptions.InsufficientFundsException("User doesn't have the given amount of " + from +
                        " currency on their account. Account amount is " + availableAmount + " and the specified amount is " + quantity);
            }
        } catch (CustomExceptions.EntityDoesntExistException | CustomExceptions.InvalidRequestParameterValueException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private static ResponseEntity<?> getResponseEntity(@PathVariable String from, @PathVariable String to, @PathVariable double quantity,
                                                       String email, CryptoExchangeProxy cryptoExchangeProxy, CryptoWalletProxy cryptoWalletProxy)
            throws CustomExceptions.EntityDoesntExistException {
        ResponseEntity<CryptoConversion> response = cryptoExchangeProxy.getExchange(from, to);
        if (response == null) {
            throw new CustomExceptions.EntityDoesntExistException("Crypto exchange data not found!");
        }
        CryptoConversion responseBody = response.getBody();
        CryptoConversion newConversion = new CryptoConversion(from, to, responseBody.getConversionMultiple(), BigDecimal.valueOf(quantity),
                responseBody.getConversionMultiple().multiply(BigDecimal.valueOf(quantity)),responseBody.getEnvironment() + " feign");
        CryptoWalletDto updatedBalance = cryptoWalletProxy.updateCryptoWalletBalance(email, BigDecimal.valueOf(quantity),
                from, newConversion.getTotalConversionAmount(), to).getBody();
        return ResponseEntity.ok().body(
                "User : " + email + System.lineSeparator() +
                        "BTC amount : " + updatedBalance.getBTC_amount().toString() + System.lineSeparator() +
                        "ETH amount : " + updatedBalance.getETH_amount().toString() + System.lineSeparator() +
                        "LTC amount : " + updatedBalance.getLTC_amount().toString() + System.lineSeparator() +
                        "XRP amount : " + updatedBalance.getXRP_amount().toString() + System.lineSeparator() +
                        "User successfully converted " + quantity
                        + " of " + from + " to " + newConversion.getTotalConversionAmount() + " of " + to);
    }
}
