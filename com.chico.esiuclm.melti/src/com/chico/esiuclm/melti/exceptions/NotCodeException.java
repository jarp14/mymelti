package com.chico.esiuclm.melti.exceptions;

@SuppressWarnings("serial")
public class NotCodeException extends Exception {
	public NotCodeException() { 
			super("No existe codigo");
	}
}