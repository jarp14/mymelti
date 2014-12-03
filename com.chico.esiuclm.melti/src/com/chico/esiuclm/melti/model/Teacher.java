package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.persistence.DAOTeacher;

public class Teacher extends User {
	
	private String token;
	
	public Teacher(String id, String first, String last, String email, String courseId) {
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
		DAOTeacher.getSolutionsDB(task_id, course_id);
	}

	// Pide los estudiantes del curso
	public void getStudents(String course_id) throws ClassNotFoundException, SQLException {
		DAOTeacher.getStudentsDB(course_id);
	}

	// Sube la calificacion de la tarea de un alumno
	public void uploadQualification(Solution solution) throws ClassNotFoundException, SQLException {
		DAOTeacher.addQualificationDB(solution);
	}
	
}
