package com.currency.conversion.currencyconversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {

	@GetMapping("/bank-account/user/{email}")
	public ResponseEntity<BankAccountDto> getBankAccount(@PathVariable String email);
	@PostMapping("/bank-account/create/{email}")
	public ResponseEntity<BankAccountDto> createBankAccount(@PathVariable String email);
	@PutMapping("/bank-account/update/{oldEmail}/for/{newEmail}")
	public ResponseEntity<BankAccountDto> updateBankAccountEmail(@PathVariable String oldEmail, @PathVariable String newEmail);
	@PutMapping("/bank-account/update/user/{email}/subtract/{quantityS}from/{currS}/add/{quantityA}to/{currA}")
	public ResponseEntity<BankAccountDto> updateBankAccountBalance(@PathVariable String email, @PathVariable BigDecimal quantityS, @PathVariable String currS,
																   @PathVariable BigDecimal quantityA, @PathVariable String currA);
	@DeleteMapping("/bank-account/delete/{email}")
	public ResponseEntity<Boolean> deleteBankAccount(@PathVariable String email);
}
