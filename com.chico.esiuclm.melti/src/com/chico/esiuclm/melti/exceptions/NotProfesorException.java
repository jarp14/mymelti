package com.chico.esiuclm.melti.exceptions;

@SuppressWarnings("serial")
public class NotProfesorException extends Exception {
	public NotProfesorException() { 
		super("No tienes privilegios de profesor");
	}
}
