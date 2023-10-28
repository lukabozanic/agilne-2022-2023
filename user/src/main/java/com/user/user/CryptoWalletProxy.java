package com.user.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {

    @PostMapping("/crypto-wallet/create/{email}")
    public ResponseEntity<CryptoWalletDto> createCryptoWallet(@PathVariable String email);
    @PutMapping("/crypto-wallet/update/{oldEmail}/for/{newEmail}")
    public ResponseEntity<CryptoWalletDto> updateCryptoWalletEmail(@PathVariable String oldEmail, @PathVariable String newEmail);
    @DeleteMapping("/crypto-wallet/delete/{email}")
    public ResponseEntity<Boolean> deleteCryptoWallet(@PathVariable String email);
}