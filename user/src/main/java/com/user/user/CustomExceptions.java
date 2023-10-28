package com.user.user;

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

	public static class MethodExecutionPermissionDeniedException extends Exception{
		public MethodExecutionPermissionDeniedException(String message){
			super(message);
		}
	}

	public static class OwnerAlreadyExistsException extends Exception{
		public OwnerAlreadyExistsException(String message){
			super(message);
		}
	}
}
