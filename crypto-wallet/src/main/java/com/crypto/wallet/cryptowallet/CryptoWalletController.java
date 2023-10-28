package com.crypto.wallet.cryptowallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class CryptoWalletController {

    @Autowired
    private Environment environment;

    @Autowired
    private CryptoWalletRepository repository;

    @Autowired
    private UserProxy proxy;

    @GetMapping("/crypto-wallet/user/{email}")
    public CryptoWallet getCryptoWallet(@PathVariable String email) throws Exception{

        try {
            String port = environment.getProperty("local.server.port");

            CryptoWallet cryptoWallet = repository.findByEmail(email);

            if(cryptoWallet == null) {
                throw new CustomExceptions.EntityDoesntExistException("Crypto wallet for " + email + " not found!");
            }

            return new CryptoWallet(email, cryptoWallet.getBTC_amount(),
                    cryptoWallet.getETH_amount(), cryptoWallet.getLTC_amount(), cryptoWallet.getXRP_amount(),
                    port);
        } catch (CustomExceptions.EntityDoesntExistException e) {
            throw e;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @PostMapping("/crypto-wallet/create/{email}")
    public ResponseEntity<CryptoWallet> createCryptoWallet(@PathVariable String email) throws Exception{

        try {
            String port = environment.getProperty("local.server.port");
            CryptoWallet existingCryptoWallet = repository.findByEmail(email);

            if(existingCryptoWallet != null) {
                throw new CustomExceptions.EntiyWithEmailAlreadyExistsException("Crypto wallet for " + email + " already exists!");
            }

            CryptoWallet newCryptoWallet = new CryptoWallet(email, port);

            repository.save(newCryptoWallet);

            return ResponseEntity.status(201).body(newCryptoWallet);

        } catch (CustomExceptions.EntiyWithEmailAlreadyExistsException e) {
            throw e;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @PutMapping("/crypto-wallet/update/{email}")
    public ResponseEntity<CryptoWallet> updateBankAccountEntity(@PathVariable String email, @RequestBody CryptoWallet updatedCryptoWallet) throws Exception{

        try {
            CryptoWallet existingCryptoWallet = repository.findByEmail(email);

            if(existingCryptoWallet == null) {
                throw new CustomExceptions.EntityDoesntExistException("Crypto wallet for " + email + " not found!");
            }

            existingCryptoWallet.setBTC_amount(existingCryptoWallet.getBTC_amount());
            existingCryptoWallet.setETH_amount(existingCryptoWallet.getETH_amount());
            existingCryptoWallet.setLTC_amount(existingCryptoWallet.getLTC_amount());
            existingCryptoWallet.setXRP_amount(existingCryptoWallet.getXRP_amount());

            repository.save(existingCryptoWallet);
            return ResponseEntity.status(200).body(existingCryptoWallet);
        } catch (CustomExceptions.EntityDoesntExistException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/crypto-wallet/update/{oldEmail}/for/{newEmail}")
    public ResponseEntity<CryptoWallet> updateCryptoWalletEmail(@PathVariable String oldEmail, @PathVariable String newEmail) throws Exception{

        try {
            CryptoWallet existingCryptoWallet = repository.findByEmail(oldEmail);

            if(existingCryptoWallet == null) {
                throw new CustomExceptions.EntityDoesntExistException("Crypto wallet for " + oldEmail + " not found!");
            }

            UserDto user = proxy.getUserByEmail(newEmail).getBody();

            if(user == null){
                throw new CustomExceptions.EntityDoesntExistException("No existing user with email : " + oldEmail);
            }

            existingCryptoWallet.setEmail(newEmail);

            repository.save(existingCryptoWallet);
            return ResponseEntity.status(200).body(existingCryptoWallet);
        } catch (CustomExceptions.EntityDoesntExistException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/crypto-wallet/update/user/{email}/subtract/{quantityS}/from/{currS}/add/{quantityA}to/{currA}")
    public ResponseEntity<CryptoWallet> updateCryptoWalletBalance(@PathVariable String email, @PathVariable BigDecimal quantityS,
                                                                @PathVariable String currS, @PathVariable BigDecimal quantityA, @PathVariable String currA) throws Exception{
        try {
            CryptoWallet userCryptoWallet = repository.findByEmail(email);

            if(userCryptoWallet == null){
                throw new CustomExceptions.EntityDoesntExistException("There is no bank account for user with email " + email);
            }

            subtract(currS, quantityS, userCryptoWallet);
            add(currA, quantityA, userCryptoWallet);

            repository.save(userCryptoWallet);
            return ResponseEntity.status(201).body(userCryptoWallet);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/crypto-wallet/update/user/{email}/change_by/{quantity}/from/{curr}/increase/or/decrease/{in_de_crease}")
    public ResponseEntity<CryptoWallet> changeCryptoWalletBalance(@PathVariable String email, @PathVariable BigDecimal quantity,
                                                                  @PathVariable String curr, @PathVariable Boolean in_de_crease) throws Exception{
        try {
            CryptoWallet userCryptoWallet = repository.findByEmail(email);

            if(userCryptoWallet == null){
                throw new CustomExceptions.EntityDoesntExistException("There is no bank account for user with email " + email);
            }

            if(in_de_crease){
                add(curr, quantity, userCryptoWallet);
            } else {
                subtract(curr, quantity, userCryptoWallet);
            }

            repository.save(userCryptoWallet);
            return ResponseEntity.status(201).body(userCryptoWallet);
        } catch (CustomExceptions.EntityDoesntExistException e){
            throw e;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }




    @DeleteMapping("/crypto-wallet/delete/{email}")
    public ResponseEntity<Boolean> deleteCryptoWallet(@PathVariable String email) throws Exception{
        try {
            CryptoWallet existingCryptoWallet = repository.findByEmail(email);

            if(existingCryptoWallet == null) {
                throw new CustomExceptions.EntityDoesntExistException("Crypto wallet  for " + email + " not found!");
            }

            repository.delete(existingCryptoWallet);
            repository.save(existingCryptoWallet);

            return ResponseEntity.status(204).body(true);
        } catch (CustomExceptions.EntityDoesntExistException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private static void subtract(String curr, BigDecimal quantity, CryptoWallet wallet){

        if (curr.equals("BTC")) {
            wallet.setBTC_amount(wallet.getBTC_amount().subtract(quantity));
        } else if (curr.equals("ETH")) {
            wallet.setETH_amount(wallet.getETH_amount().subtract(quantity));
        } else if (curr.equals("LTC")) {
            wallet.setLTC_amount(wallet.getLTC_amount().subtract(quantity));
        } else if (curr.equals("XRP")) {
            wallet.setXRP_amount(wallet.getXRP_amount().subtract(quantity));
        }
    }

    private static void add(String curr, BigDecimal quantity, CryptoWallet wallet){

        if (curr.equals("BTC")) {
            wallet.setBTC_amount(wallet.getBTC_amount().add(quantity));
        } else if (curr.equals("ETH")) {
            wallet.setETH_amount(wallet.getETH_amount().add(quantity));
        } else if (curr.equals("LTC")) {
            wallet.setLTC_amount(wallet.getLTC_amount().add(quantity));
        } else if (curr.equals("XRP")) {
            wallet.setXRP_amount(wallet.getXRP_amount().add(quantity));
        }
    }
}
