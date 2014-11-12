package com.chico.esiuclm.melti.model;

public class Profesor extends User {
	
	private String token;
	
	public Profesor(int id, String first, String last, String email, String role, String token) {
		super(id, first, last, email, role);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
