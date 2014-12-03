package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.exceptions.SolvedErrorException;

public class MeltiServer {
	
	private static MeltiServer yo;
	private Student the_student; // Guarda el alumno que usa el plugin
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
	
	/*
	 * Llamadas, acciones del profesor
	 */
	public void addQualificationDB(Solution solution) throws ClassNotFoundException, SQLException {
		the_teacher.uploadQualification(solution); // Sube la calificacion de la tarea de un alumno
	}
	public void getSolutionsDB(String task_id, String course_id) throws ClassNotFoundException, SQLException {
		this.the_teacher.getSolutions(task_id, course_id); // Recoge las soluciones de la BBDD y actualiza el curso
	}	
	public void getStudentsDB(String course_id) throws ClassNotFoundException, SQLException {
		this.the_teacher.getStudents(course_id); // Recoge los estudiantes de la BBDD y actualiza el curso
	}
	
	/**
	 * Getters y Setters
	 */	
	public void setActiveCourse(Course course) {
		this.the_course = course;		
	}
	public void setActiveTask(Task task) {
		this.the_task = task;
	}	
	public void setActiveStudent(Student student) {
		this.the_student = student;
	}	
	public void setActiveTeacher(Teacher teacher) {
		this.the_teacher = teacher;	
	}	
	public Student getActiveStudent() {
		return this.the_student;
	}	
	public Task getActiveTask() {
		return this.the_task;
	}	
	public Course getActiveCourse() {
		return this.the_course;
	}	
	public Teacher getActiveTeacher() {
		return this.the_teacher;
	}
	
}
