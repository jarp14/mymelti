package com.chico.esiuclm.melti.exceptions;

@SuppressWarnings("serial")
public class TeacherNotLoggedException extends Exception {
	public TeacherNotLoggedException() { 
		super("El profesor debe conectarse previamente en Moodle para realizar esta acción");
	}
}
