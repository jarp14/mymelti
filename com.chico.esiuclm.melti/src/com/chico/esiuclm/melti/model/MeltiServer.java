package com.chico.esiuclm.melti.model;

import java.sql.SQLException;
import java.util.ArrayList;

public class MeltiServer {
	
	private static MeltiServer yo;
	private Student the_student;
	private Profesor the_profesor;
	private Task the_task;
	private Course the_course;
	
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
		this.the_course.getCourseTasks().add(task);
		task.add(); // Agrega la tarea a la BBDD
	}
	
	public void addSolutionDB(Solution solution) throws ClassNotFoundException, SQLException {
		this.the_course.getCourseSolutions().add(solution);
		solution.add(); // Agrega la resolucion de una tarea a la BBDD
	}
	
	public void addStudentDB(Student student) throws ClassNotFoundException, SQLException {
		this.the_course.getCourseStudents().add(student);
		student.add(); // Agrega el estudiante a la BBDD
	}
	
	public void getSolutionsDB(String task_id, String course_id) throws ClassNotFoundException, SQLException {
		this.the_profesor.getSolutions(task_id, course_id); // Recoge y actualiza el curso con las soluciones
	}
	
	public void getStudentsDB(String course_id) throws ClassNotFoundException, SQLException {
		this.the_profesor.getStudents(course_id);
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
	public void setActiveProfesor(Profesor profesor) {
		this.the_profesor = profesor;	
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
	public Profesor getActiveProfesor() {
		return this.the_profesor;
	}
	
}
