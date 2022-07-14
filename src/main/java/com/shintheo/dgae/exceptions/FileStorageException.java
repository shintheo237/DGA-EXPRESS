package com.shintheo.dgae.exceptions;

public class FileStorageException extends RuntimeException{

	private static final long serialVersionUID = -5469254859070175953L;

	public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
	
	

}
