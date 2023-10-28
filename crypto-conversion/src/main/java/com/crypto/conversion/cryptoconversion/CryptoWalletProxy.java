package com.crypto.conversion.cryptoconversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {

    @GetMapping("/crypto-wallet/user/{email}")
    public ResponseEntity<CryptoWalletDto> getCryptoWallet(@PathVariable String email);
    @PostMapping("/crypto-wallet/create/{email}")
    public ResponseEntity<CryptoWalletDto> createCryptoWallet(@PathVariable String email);
    @PutMapping("/crypto-wallet/update/{oldEmail}/for/{newEmail}")
    public ResponseEntity<CryptoWalletDto> updateCryptoWalletEmail(@PathVariable String oldEmail, @PathVariable String newEmail);
    @PutMapping("/crypto-wallet/update/user/{email}/subtract/{quantityS}/from/{currS}/add/{quantityA}to/{currA}")
    public ResponseEntity<CryptoWalletDto> updateCryptoWalletBalance(@PathVariable String email, @PathVariable BigDecimal quantityS, @PathVariable String currS,
                                                                   @PathVariable BigDecimal quantityA, @PathVariable String currA);
    @DeleteMapping("/crypto-wallet/delete/{email}")
    public ResponseEntity<Boolean> deleteCryptoWallet(@PathVariable String email);
}
