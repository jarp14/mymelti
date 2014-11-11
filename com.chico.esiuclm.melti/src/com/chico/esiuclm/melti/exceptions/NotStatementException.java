package com.chico.esiuclm.melti.exceptions;

@SuppressWarnings("serial")
public class NotStatementException extends Exception {
	public NotStatementException() { 
		super("No existe enunciado");
	}
}