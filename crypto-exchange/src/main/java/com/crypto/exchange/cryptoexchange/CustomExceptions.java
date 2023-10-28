package com.crypto.exchange.cryptoexchange;

public class CustomExceptions {

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

	public static class MethodExecutionPermissionDenied extends Exception{
		public MethodExecutionPermissionDenied(String message){
			super(message);
		}
	}
}
