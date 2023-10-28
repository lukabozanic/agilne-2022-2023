package com.transferservice.transferservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {

	@GetMapping("/bank-account/user/{email}")
	ResponseEntity<BankAccountDto> getBankAccount(@PathVariable String email);
	@PostMapping("/bank-account/create/{email}")
	ResponseEntity<BankAccountDto> createBankAccount(@PathVariable String email);
	@PutMapping("/bank-account/update/{oldEmail}/for/{newEmail}")
	ResponseEntity<BankAccountDto> updateBankAccountEmail(@PathVariable String oldEmail, @PathVariable String newEmail);
	@PutMapping("/bank-account/update/user/{email}/subtract/{quantityS}from/{currS}/add/{quantityA}to/{currA}")
	ResponseEntity<BankAccountDto> updateBankAccountBalance(@PathVariable String email, @PathVariable BigDecimal quantityS, @PathVariable String currS,
															@PathVariable BigDecimal quantityA, @PathVariable String currA);
	@PutMapping("/bank-account/update/user/{email}/change_by/{quantity}/from/{curr}/increase/or/decrease/{in_de_crease}")
	ResponseEntity<BankAccountDto> changeBankAccountBalance(@PathVariable String email, @PathVariable BigDecimal quantity, @PathVariable String curr,
															@PathVariable Boolean in_de_crease);
	@DeleteMapping("/bank-account/delete/{email}")
	ResponseEntity<Boolean> deleteBankAccount(@PathVariable String email);
}
