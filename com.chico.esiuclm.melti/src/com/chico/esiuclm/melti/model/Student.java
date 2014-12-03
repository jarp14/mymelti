package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.exceptions.SolvedErrorException;
import com.chico.esiuclm.melti.persistence.DAOStudent;

public class Student extends User {

	public Student(String id, String first, String last, String email, String courseId) {
		super(id, first, last, email, "Learner", courseId);
	}
	
	// Permite añadir el alumno a la BBDD
	public void add() throws ClassNotFoundException, SQLException {
		DAOStudent.addStudentDB(this);
	}
	
	// El alumno sube una solucion a la BBDD
	public void uploadSolution(Solution solution) throws ClassNotFoundException, SQLException {
		DAOStudent.addSolutionDB(solution);
	}

	// Permite comprobar si su solucion a una tarea ya fue subida
	public void checkIfSolutionSolvedDB(String[] task_context) throws ClassNotFoundException, SQLException, SolvedErrorException {
		DAOStudent.checkIfSolutionSolvedDB(task_context);
	}

}
