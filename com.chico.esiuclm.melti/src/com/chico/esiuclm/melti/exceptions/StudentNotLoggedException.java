package com.chico.esiuclm.melti.exceptions;

@SuppressWarnings("serial")
public class StudentNotLoggedException extends Exception {
	public StudentNotLoggedException() { 
		super("El alumno debe conectarse previamente en Moodle para realizar esta acción");
	}
}
