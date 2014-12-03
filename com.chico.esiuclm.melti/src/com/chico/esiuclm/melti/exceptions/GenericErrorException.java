package com.chico.esiuclm.melti.exceptions;

@SuppressWarnings("serial")
public class GenericErrorException extends Exception {
	public GenericErrorException() { 
		super("Error inesperado");
	}
}
