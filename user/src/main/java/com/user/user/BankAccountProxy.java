package com.user.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {

	@PostMapping("/bank-account/create/{email}")
	public ResponseEntity<BankAccountDto> createBankAccount(@PathVariable String email);
	@PutMapping("/bank-account/update/{oldEmail}/for/{newEmail}")
	public ResponseEntity<BankAccountDto> updateBankAccountEmail(@PathVariable String oldEmail, @PathVariable String newEmail);
	@DeleteMapping("/bank-account/delete/{email}")
	public ResponseEntity<Boolean> deleteBankAccount(@PathVariable String email);
}
