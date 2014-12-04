package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.exceptions.SolvedErrorException;

public class MeltiServer {
	
	private static MeltiServer yo;
	private Student the_student; // Guarda el alumno que esta usando el plugin
	private Teacher the_teacher; // Guarda el profesor que esta usando el plugin
	private Task the_task; // La tarea que es accedida
	private Course the_course; // El curso que es accedido
	
	private MeltiServer() {}
	
	// singleton
	public static MeltiServer get() {
		if (yo==null)
			yo = new MeltiServer();
		return yo;
	}
	
	/** 
	 * Peticiones, consultas, llamadas... a la BBDD
	 */
	public void addCourseDB(Course course) throws ClassNotFoundException, SQLException {
		course.add(); // Agrega el curso a la BBDD	
	}
	public void addTaskDB(Task task) throws ClassNotFoundException, SQLException {
		task.add(); // Agrega la tarea a la BBDD
	}
	public void addStudentDB(Student student) throws ClassNotFoundException, SQLException {
		student.add(); // Agrega el alumno a la BBDD
	}
	
	/*
	 * Lladamas, acciones del alumno
	 */
	public void addSolutionDB(Solution solution) throws ClassNotFoundException, SQLException {
		the_student.uploadSolution(solution); // Agrega la resolucion de una tarea a la BBDD
	}
	public void checkIfSolutionSolvedDB(String[] task_context) throws ClassNotFoundException, SQLException, SolvedErrorException {
		the_student.checkIfSolutionSolvedDB(task_context); // Comprueba si subio ya la resolucion de una tarea
	}
	public void getMySolutionsDB(String user_id) throws ClassNotFoundException, SQLException {
		the_student.getMySolutionsDB(user_id);
	}
	
	/*
	 * Llamadas, acciones del profesor
	 */
	public void addQualificationDB(Solution solution) throws ClassNotFoundException, SQLException {
		the_teacher.uploadQualification(solution); // Sube la calificacion de la tarea de un alumno
	}
	public void getSolutionsDB(String task_id, String course_id) throws ClassNotFoundException, SQLException {
		the_teacher.getSolutions(task_id, course_id); // Recoge las soluciones de la BBDD y actualiza el curso
	}	
	public void getStudentsDB(String course_id) throws ClassNotFoundException, SQLException {
		the_teacher.getStudents(course_id); // Recoge los estudiantes de la BBDD y actualiza el curso
	}
	
	/**
	 * Getters y Setters
	 */	
	public void setActiveCourse(Course course) {
		the_course = course;		
	}	
	public Course getActiveCourse() {
		return the_course;
	}
	public Student getActiveStudent() {
		return the_student;
	}
	public void setActiveStudent(Student s) {
		the_student = s;
	}
	public Teacher getActiveTeacher() {
		return the_teacher;
	}
	public void setActiveTeacher(Teacher t) {
		the_teacher = t;
	}
	public Task getActiveTask() {
		return the_task;
	}
	public void setActiveTask(Task t) {
		the_task = t;
	}
	
}
