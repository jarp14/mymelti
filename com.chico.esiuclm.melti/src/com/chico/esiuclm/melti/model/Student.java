package com.chico.esiuclm.melti.model;

import java.sql.SQLException;
import java.util.ArrayList;

import com.chico.esiuclm.melti.exceptions.SolvedErrorException;
import com.chico.esiuclm.melti.persistence.DAOStudent;

public class Student extends User {
	
	private ArrayList<Solution> my_solutions; // Las soluciones asociadas a un alumno
	private ArrayList<Task> my_tasks; // Las tareas asociadas a un alumno
	private ArrayList<Course> my_courses; // La informacion de los cursos asociados a un alumno

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
	
	public void getMyCoursesDB(String user_id) throws ClassNotFoundException, SQLException {
		DAOStudent.getMyCoursesDB(user_id);
	}
	
	public void getMyTasksDB(String user_id) throws ClassNotFoundException, SQLException {
		DAOStudent.getMyTasksDB(user_id);
	}
	
	/**
	 * Getters y Setters
	 */
	public ArrayList<Solution> getMySolutions() {
		return my_solutions;
	}
	public void setMySolutions(ArrayList<Solution> solutions) {
		my_solutions = solutions;
	}
	public ArrayList<Task> getMyTasks() {
		return my_tasks;
	}
	public void setMyTasks(ArrayList<Task> tasks) {
		my_tasks = tasks;
	}
	public void setMyCourses(ArrayList<Course> courses) {
		my_courses = courses;
	}
	public ArrayList<Course> getMyCourses() {
		return my_courses;
	}

}
