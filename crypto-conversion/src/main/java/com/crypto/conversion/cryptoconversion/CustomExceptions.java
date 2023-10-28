package com.crypto.conversion.cryptoconversion;

public class CustomExceptions {

	public static class EntiyWithEmailAlreadyExistsException extends Exception {
		public EntiyWithEmailAlreadyExistsException(String message){
			
			super(message);
		}
	}

	public static class InvalidRequestParameterValueException extends Exception{
		public InvalidRequestParameterValueException(String message){
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

	public static class InsufficientFundsException extends Exception{
		public InsufficientFundsException(String message){
			super(message);
		}
	}
}
