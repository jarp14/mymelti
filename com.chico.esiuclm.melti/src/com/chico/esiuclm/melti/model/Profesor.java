package com.chico.esiuclm.melti.model;

public class Profesor extends User {
	
	private String token;
	
	public Profesor(String id, String first, String last, String email, String role, String token, String courseId) {
		super(id, first, last, email, role, courseId);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
