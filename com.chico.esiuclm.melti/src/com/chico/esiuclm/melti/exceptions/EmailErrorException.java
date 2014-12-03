package com.chico.esiuclm.melti.exceptions;

@SuppressWarnings("serial")
public class EmailErrorException extends Exception {
	public EmailErrorException() { 
		super("El usuario no coincide");
	}
}
