package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;
import com.chico.esiuclm.melti.persistence.DAOStudent;

public class Student extends User {

	public Student(int id, String first, String last, String email, String role) {
		super(id, first, last, email, role);
	}
	
	public void add() throws ClassNotFoundException, SQLException, GenericErrorException {
		DAOStudent.addStudentDB(this);
	}

}
