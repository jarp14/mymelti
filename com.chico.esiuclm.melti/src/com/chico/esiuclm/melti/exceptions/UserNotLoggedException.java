package com.chico.esiuclm.melti.exceptions;

@SuppressWarnings("serial")
public class UserNotLoggedException extends Exception {
	public UserNotLoggedException() { 
		super("El usuario debe conectarse previamente en Moodle");
	}
}