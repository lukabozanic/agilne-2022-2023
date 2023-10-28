package com.bank.account.bankaccount;

public class CustomExceptions{

	public static class EntiyWithEmailAlreadyExistsException extends Exception {
		public EntiyWithEmailAlreadyExistsException(String message){
			
			super(message);
		}
	}
	
	public static class EntityDoesntExistException extends Exception{
		public EntityDoesntExistException(String message){
			super(message);
		}
	}
}
