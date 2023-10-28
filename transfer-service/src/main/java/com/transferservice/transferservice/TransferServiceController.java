package com.transferservice.transferservice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
public class TransferServiceController {

    @Autowired
    private BankAccountProxy bankAccountProxy;

    @Autowired
    private CryptoWalletProxy cryptoWalletProxy;

    @Value("#{'${crypto.currencies}'.split(',')}")
    private List<String> cryptoCurrencies;

    @Value("#{'${fiat.currencies}'.split(',')}")
    private List<String> fiatCurrencies;

    @GetMapping("/transfer-service/currency/{currency}/amount/{amount}/to/user/{email}")
    public ResponseEntity<?> transferService(@PathVariable String currency, @PathVariable BigDecimal amount, @PathVariable String email, HttpServletRequest request) throws  Exception{
        try{
            String loggedUser = request.getHeader("X-User-Email");

            BigDecimal amountWithProvision = amount.add(amount.divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP));
            System.out.println(amountWithProvision + " " + amount.divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP));
            if(cryptoCurrencies.contains(currency)){
                CryptoWalletDto userFromCryptoWallet = cryptoWalletProxy.getCryptoWallet(loggedUser).getBody();
                CryptoWalletDto userToCryptoWallet = cryptoWalletProxy.getCryptoWallet(email).getBody();
                if(userToCryptoWallet == null || userFromCryptoWallet == null){
                    throw new CustomExceptions.EntityDoesntExistException("Crypto wallet of user doesnt exist! ");
                }
                BigDecimal amountAvailable = getCryptoAmount(userFromCryptoWallet, currency);
                if(amountAvailable.compareTo(amountWithProvision) >= 0){
                    cryptoWalletProxy.changeCryptoWalletBalance(loggedUser, amountWithProvision, currency, false);
                    cryptoWalletProxy.changeCryptoWalletBalance(email, amount, currency, true);
                    return ResponseEntity.ok().body(
                            "User : " + loggedUser + System.lineSeparator() +
                                    " has successfully transferred " + amount + " of " + currency +
                                    " to user " + email);
                } else {
                    throw new CustomExceptions.InsufficientFundsException("Amount of " + currency + " user has in his crypto wallet is less than wanted quantity: " + amount);
                }
            } else if(fiatCurrencies.contains(currency)){
                BankAccountDto userToBankAccount = bankAccountProxy.getBankAccount(email).getBody();
                BankAccountDto userFromBankAccount = bankAccountProxy.getBankAccount(loggedUser).getBody();
                if(userFromBankAccount == null || userToBankAccount == null){
                    throw new CustomExceptions.EntityDoesntExistException("Bank Account of user doesnt exist! ");
                }
                BigDecimal amountAvailable = getFiatAmount(userFromBankAccount, currency);
                if(amountAvailable.compareTo(amountWithProvision) >= 0){
                    bankAccountProxy.changeBankAccountBalance(loggedUser, amountWithProvision, currency, false);
                    bankAccountProxy.changeBankAccountBalance(email, amount, currency, true);
                    return ResponseEntity.ok().body(
                            "User : " + loggedUser + System.lineSeparator() +
                                    " has successfully transferred " + amount + " of " + currency +
                                    " to user " + email);
                } else {
                    throw new CustomExceptions.InsufficientFundsException("Amount of " + currency + " user has in his crypto wallet is less than wanted quantity: " + amount);
                }
            } else {
                throw  new CustomExceptions.InvalidRequestParameterValueException("Provided currency doesnt exist in current system! ");
            }

        } catch (CustomExceptions.InvalidRequestParameterValueException | CustomExceptions.InsufficientFundsException e){
            throw e;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private BigDecimal getCryptoAmount(CryptoWalletDto userCryptoWallet, String currency) {
        switch (currency) {
            case "BTC":
                return userCryptoWallet.getBTC_amount();
            case "ETH":
                return userCryptoWallet.getETH_amount();
            case "LTC":
                return userCryptoWallet.getLTC_amount();
            default:
                return userCryptoWallet.getXRP_amount();
        }
    }

    private BigDecimal getFiatAmount(BankAccountDto userBankAccount, String currency) {
        switch (currency) {
            case "EUR":
                return userBankAccount.getEUR_amount();
            case "USD":
                return userBankAccount.getUSD_amount();
            case "RSD":
                return userBankAccount.getRSD_amount();
            case "GBP":
                return userBankAccount.getGBP_amount();
            default:
                return userBankAccount.getCHF_amount();
        }
    }
}
