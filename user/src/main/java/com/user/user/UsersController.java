package com.user.user;

import com.user.user.CustomExceptions.EntityDoesntExistException;
import com.user.user.CustomExceptions.EntiyWithEmailAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsersController {
	
	@Autowired
	private UsersRepository repository;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;

	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;
	
	@Autowired
	private Environment environment;


	SecurityContext securityContext = SecurityContextHolder.getContext();

	// Get the Authentication object
	Authentication authentication = securityContext.getAuthentication();

	@GetMapping("users/all")
	public ResponseEntity<List<Users>> getAllUsers() throws Exception{

		try {
			List<Users> allUsers = repository.findAll();
			return ResponseEntity.status(200).body(allUsers);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	
	@GetMapping("/users/{email}")
	public Users getUser(@PathVariable String email) throws Exception {
		
		try {
			String port = environment.getProperty("local.server.port");
			
			Users user = repository.findByEmail(email);
			
			if(user == null) {
				throw new EntityDoesntExistException("User with " + email + "not found");
			}
			
			return new Users(user.getId(), email, user.getPassword(), user.getRole(), port);
		} catch (EntityDoesntExistException e) {
	        throw e; 
	    } catch (Exception ex) {
	        throw new Exception(ex.getMessage());
	    }
	}
	
	@PostMapping("/users/create")
	public ResponseEntity<Users> createUser(@RequestBody Users user, HttpServletRequest request) throws Exception {
		try {
			String role = request.getHeader("X-User-Role");
			Users existingUser = repository.findByEmail(user.getEmail());
			Users owner = repository.findByRole(Role.OWNER);
			if(existingUser != null) {
				throw new EntiyWithEmailAlreadyExistsException("User with given email already exists in database ");
			}
			if(owner != null && user.getRole().equals(Role.OWNER)){
				throw new CustomExceptions.OwnerAlreadyExistsException("Owner already exists in this database. ");
			}
			if(role.endsWith("ADMIN") && (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.OWNER))) {
				throw new CustomExceptions.MethodExecutionPermissionDeniedException("Method not allowed for following role: " + role);
			}
			
			String port = environment.getProperty("local.server.port");
			
			user.setEnvironment(port);
			Users createdUser = repository.save(user);
			if(createdUser.getRole().equals(Role.USER)){
				bankAccountProxy.createBankAccount(createdUser.getEmail());
				cryptoWalletProxy.createCryptoWallet(createdUser.getEmail());
			}
			return ResponseEntity.status(201).body(createdUser);
		} catch (EntiyWithEmailAlreadyExistsException e) {
			throw e;
		} catch (CustomExceptions.OwnerAlreadyExistsException e){
				throw e;
		} catch (CustomExceptions.MethodExecutionPermissionDeniedException e) {
				throw e;
	    } catch (Exception ex) {
	        throw new Exception(ex.getMessage());
	    }
	}
	
	@PutMapping("/users/update/{email}")
	public ResponseEntity<Users> updateUser(@PathVariable("email") String email, @RequestBody Users updateUser) throws Exception{
		
		try {
			Users existingUser = repository.findByEmail(email);
			Users checkNewEmail = repository.findByEmail(updateUser.getEmail());
			if(existingUser == null) {
				throw new EntityDoesntExistException("User with " + email + "not found");
			}
			if(checkNewEmail != null) {
				throw new EntiyWithEmailAlreadyExistsException("User with given email already exists in database");
			}
			
			String port = environment.getProperty("local.server.port");
			
			existingUser.setEmail(updateUser.getEmail());
			existingUser.setPassword(updateUser.getPassword());
			existingUser.setRole(updateUser.getRole());
			existingUser.setEnvironment(port);
			
			Users updatedUser = repository.save(existingUser);
			if(updatedUser.getRole().equals(Role.USER)) {
				bankAccountProxy.updateBankAccountEmail(email, updatedUser.getEmail());
				cryptoWalletProxy.updateCryptoWalletEmail(email, updatedUser.getEmail());
			}
			return ResponseEntity.status(201).body(updatedUser);
		} catch (EntityDoesntExistException e) {
	        throw e; 
	    } catch (EntiyWithEmailAlreadyExistsException e) {
			throw e;
		} catch (Exception ex) {
	        throw new Exception(ex.getMessage());
	    }
	}
	
	@DeleteMapping("users/delete/{email}")
	public void deleteUser(@PathVariable("email") String email) throws Exception{
		
		try {
			Users existingUser = repository.findByEmail(email);
			if(existingUser == null) {
				throw new EntityDoesntExistException("User with " + email + " not found");
			}
			if(existingUser.getRole().equals(Role.USER)) {
				bankAccountProxy.deleteBankAccount(email);
				cryptoWalletProxy.deleteCryptoWallet(email);
			}
			repository.delete(existingUser);
			repository.save(null);
		} catch (EntityDoesntExistException e) {
	        throw e; 
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
}
