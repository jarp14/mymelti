package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.persistence.DAOProfesor;

public class Profesor extends User {
	
	private String token;
	
	public Profesor(String id, String first, String last, String email, String courseId) {
		super(id, first, last, email, "Instructor", courseId);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	// Pide las soluciones de un contexto (curso, tarea) determinado
	public void getSolutions(String task_id, String course_id) throws ClassNotFoundException, SQLException {
		DAOProfesor.getSolutionsDB(task_id, course_id);
	}

	// Pide los estudiantes del curso
	public void getStudents(String course_id) throws ClassNotFoundException, SQLException {
		DAOProfesor.getStudentsDB(course_id);
	}
	
}
