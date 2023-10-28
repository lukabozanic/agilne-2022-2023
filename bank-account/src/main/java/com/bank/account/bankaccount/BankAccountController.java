package com.bank.account.bankaccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.bankaccount.CustomExceptions.EntityDoesntExistException;
import com.bank.account.bankaccount.CustomExceptions.EntiyWithEmailAlreadyExistsException;

import java.math.BigDecimal;

@RestController
public class BankAccountController {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private BankAccountRepository bankAccountRepo;

	@Autowired
	private UserProxy proxy;
	
	@GetMapping("/bank-account/user/{email}")
	public BankAccount getBankAccount(@PathVariable String email) throws Exception{ 

		try {
		String port = environment.getProperty("local.server.port");
		
		BankAccount bankAccount = bankAccountRepo.findByEmail(email);
		
		if(bankAccount == null) {
			throw new EntityDoesntExistException("Bank account for " + email + " not found!");
		}
		
		return new BankAccount(bankAccount.getId(), email, bankAccount.getRSD_amount(), bankAccount.getEUR_amount(),
							   bankAccount.getCHF_amount(), bankAccount.getGBP_amount(), bankAccount.getUSD_amount(),
							   port);
		} catch (EntityDoesntExistException e) {
	        throw e; 
	    } catch (Exception ex) {
	        throw new Exception(ex.getMessage());
	    }
	}
	
	@PostMapping("/bank-account/create/{email}")
	public ResponseEntity<BankAccount> createBankAccount(@PathVariable String email) throws Exception{
		
		try {
			String port = environment.getProperty("local.server.port");
			BankAccount existingBankAccount = bankAccountRepo.findByEmail(email);
			
			if(existingBankAccount != null) {
				throw new EntiyWithEmailAlreadyExistsException("Bank account for " + email + " already exists!");
			}
			
			BankAccount newBankAccount = new BankAccount(email, port);
			
			bankAccountRepo.save(newBankAccount);
			
			return ResponseEntity.status(201).body(newBankAccount);
			
		} catch (EntiyWithEmailAlreadyExistsException e) {
	        throw e; 
	    } catch (Exception ex) {
	        throw new Exception(ex.getMessage());
	    }
	}
	
	@PutMapping("/bank-account/update/{email}")
	public ResponseEntity<BankAccount> updateBankAccountEntity(@PathVariable String email, @RequestBody BankAccount updatedBankAccount) throws Exception{
		
		try {
			BankAccount existingBankAccount = bankAccountRepo.findByEmail(email);
			
			if(existingBankAccount == null) {
				throw new EntityDoesntExistException("Bank account for " + email + " not found!");
			}
			
			existingBankAccount.setCHF_amount(updatedBankAccount.getCHF_amount());
			existingBankAccount.setEUR_amount(updatedBankAccount.getEUR_amount());
			existingBankAccount.setGBP_amount(updatedBankAccount.getGBP_amount());
			existingBankAccount.setRSD_amount(updatedBankAccount.getRSD_amount());
			existingBankAccount.setUSD_amount(updatedBankAccount.getUSD_amount());
			
			bankAccountRepo.save(existingBankAccount);
			return ResponseEntity.status(200).body(existingBankAccount);
		} catch (EntityDoesntExistException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@PutMapping("/bank-account/update/{oldEmail}/for/{newEmail}")
	public ResponseEntity<BankAccount> updateBankAccountEmail(@PathVariable String oldEmail, @PathVariable String newEmail) throws Exception{

		try {
			BankAccount existingBankAccount = bankAccountRepo.findByEmail(oldEmail);

			if(existingBankAccount == null) {
				throw new EntityDoesntExistException("Bank account for " + oldEmail + " not found!");
			}

			UserDto user = proxy.getUserByEmail(newEmail).getBody();

			if(user == null){
				throw new EntityDoesntExistException("No existing user with email : " + oldEmail);
			}

			existingBankAccount.setEmail(newEmail);

			bankAccountRepo.save(existingBankAccount);
			return ResponseEntity.status(200).body(existingBankAccount);
		} catch (EntityDoesntExistException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	@PutMapping("/bank-account/update/user/{email}/subtract/{quantityS}from/{currS}/add/{quantityA}to/{currA}")
	public ResponseEntity<BankAccount> updateBankAccountBalance(@PathVariable String email, @PathVariable BigDecimal quantityS,
																@PathVariable String currS, @PathVariable BigDecimal quantityA, @PathVariable String currA) throws Exception{
		try {
			BankAccount userBankAccount = bankAccountRepo.findByEmail(email);

			if(userBankAccount == null){
				throw new EntityDoesntExistException("There is no bank account for user with email " + email);
			}

			subtract(currS, quantityS, userBankAccount);
			add(currA, quantityA, userBankAccount);

			bankAccountRepo.save(userBankAccount);
			return ResponseEntity.status(201).body(userBankAccount);
		} catch (Exception e){
			throw new Exception(e.getMessage());
		}
	}

	@PutMapping("/bank-account/update/user/{email}/change_by/{quantity}/from/{curr}/increase/or/decrease/{in_de_crease}")
	public ResponseEntity<BankAccount> changeBankAccountBalance(@PathVariable String email, @PathVariable BigDecimal quantity,
																@PathVariable String curr, @PathVariable  Boolean in_de_crease) throws Exception{
		try {
			BankAccount userBankAccount = bankAccountRepo.findByEmail(email);

			if(userBankAccount == null){
				throw new EntityDoesntExistException("There is no bank account for user with email " + email);
			}
			if(in_de_crease){
				add(curr, quantity, userBankAccount);
			} else{
				subtract(curr, quantity, userBankAccount);
			}

			bankAccountRepo.save(userBankAccount);
			return ResponseEntity.status(201).body(userBankAccount);
		} catch (EntityDoesntExistException e){
			throw e;
		} catch (Exception e){
			throw new Exception(e.getMessage());
		}
	}


	@DeleteMapping("/bank-account/delete/{email}")
	public ResponseEntity<Boolean> deleteBankAccount(@PathVariable String email) throws Exception{
		try {
			BankAccount existingBankAccount = bankAccountRepo.findByEmail(email);
			
			if(existingBankAccount == null) {
				throw new EntityDoesntExistException("Bank account for " + email + " not found!");
			}
			
			bankAccountRepo.delete(existingBankAccount);
			
			return ResponseEntity.status(204).body(true);
		} catch (EntityDoesntExistException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	private static void subtract(String curr, BigDecimal quantity, BankAccount account){

		if (curr.equals("RSD")) {
			account.setRSD_amount(account.getRSD_amount().subtract(quantity));
		} else if (curr.equals("USD")) {
			account.setUSD_amount(account.getUSD_amount().subtract(quantity));
		} else if (curr.equals("GBP")) {
			account.setGBP_amount(account.getGBP_amount().subtract(quantity));
		} else if (curr.equals("EUR")) {
			account.setEUR_amount(account.getEUR_amount().subtract(quantity));
		} else if (curr.equals("CHF")) {
			account.setCHF_amount(account.getCHF_amount().subtract(quantity));
		}
	}

	private static void add(String curr, BigDecimal quantity, BankAccount account){

		if (curr.equals("RSD")) {
			account.setRSD_amount(account.getRSD_amount().add(quantity));
		} else if (curr.equals("USD")) {
			account.setUSD_amount(account.getUSD_amount().add(quantity));
		} else if (curr.equals("GBP")) {
			account.setGBP_amount(account.getGBP_amount().add(quantity));
		} else if (curr.equals("EUR")) {
			account.setEUR_amount(account.getEUR_amount().add(quantity));
		} else if (curr.equals("CHF")) {
			account.setCHF_amount(account.getCHF_amount().add(quantity));
		}
	}
}
















