package com.chico.esiuclm.melti.exceptions;

@SuppressWarnings("serial")
public class NotStudentException extends Exception {
	public NotStudentException() { 
		super("No tienes privilegios de estudiante");
	}
}
