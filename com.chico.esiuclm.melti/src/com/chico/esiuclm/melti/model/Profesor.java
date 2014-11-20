package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;
import com.chico.esiuclm.melti.persistence.DAOProfesor;

public class Profesor extends User {
	
	private String token;
	
	public Profesor(String id, String first, String last, String email, String role, String token) {
		super(id, first, last, email, role);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void add() throws ClassNotFoundException, SQLException, GenericErrorException {
		DAOProfesor.addProfesorDB(this);
	}
	
}
