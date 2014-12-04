package com.chico.esiuclm.melti.model;

import java.sql.SQLException;
import java.util.ArrayList;

import com.chico.esiuclm.melti.exceptions.SolvedErrorException;
import com.chico.esiuclm.melti.persistence.DAOStudent;

public class Student extends User {
	
	private ArrayList<Solution> my_solutions;

	public Student(String id, String first, String last, String email, String courseId) {
		super(id, first, last, email, "Learner", courseId);
		my_solutions = new ArrayList<Solution>();
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

	// Obtiene sus soluciones subidas
	public void getMySolutionsDB(String user_id) throws ClassNotFoundException, SQLException {
		DAOStudent.getMySolutionsDB(user_id);
	}

	public ArrayList<Solution> getMySolutions() {
		return my_solutions;
	}
	public void setMySolutions(ArrayList<Solution> solutions) {
		my_solutions = solutions;
	}

}
