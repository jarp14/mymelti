package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.persistence.DAOStudent;

public class Student extends User {

	public Student(String id, String first, String last, String email, String role) {
		super(id, first, last, email, role);
	}
	
	public void add() throws ClassNotFoundException, SQLException {
		DAOStudent.addStudentDB(this);
	}
	
	public void upload(Solution solution) throws ClassNotFoundException, SQLException {
		DAOStudent.addSolutionDB(solution);
	}

}
