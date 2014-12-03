package com.chico.esiuclm.melti.exceptions;

@SuppressWarnings("serial")
public class SolvedErrorException extends Exception {
	public SolvedErrorException() { 
		super("Esta tarea ya fue resuelta por el alumno");
	}
}
