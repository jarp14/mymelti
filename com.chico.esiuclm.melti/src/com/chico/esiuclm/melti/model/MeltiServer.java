package com.chico.esiuclm.melti.model;

import java.sql.SQLException;
import java.util.Hashtable;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;

public class MeltiServer {
	
	private static MeltiServer yo;
	private Student the_student;
	private Profesor the_profesor;
	private Task the_task;
	private Course the_course;
	private Hashtable<String, User> the_users; // Usuarios
	
	private MeltiServer() {}
	
	// singleton
	public static MeltiServer get() {
		if (yo==null)
			yo = new MeltiServer();
		return yo;
	}
	
	public void addCourse(Course course) throws ClassNotFoundException, SQLException, GenericErrorException {
		course.add(); // Agrega el curso a la BBDD	
	}
	
	public void addTask(Task task) throws ClassNotFoundException, SQLException {
		task.add(); // Agrega la tarea a la BBDD
	}
	
	public void addSolution(Solution solution) throws ClassNotFoundException, SQLException {
		solution.add(); // Agrega la resolucion de una tarea a la BBDD
	}
	
	public void addStudent(Student student) throws ClassNotFoundException, SQLException, GenericErrorException {
		if (the_users==null) {
			this.the_users = new Hashtable<String, User>();
		}
		student.add();
		this.the_users.put(student.getID(), student);
	}
	
	public Hashtable<String, User> getUsers() {
		return the_users;
	}
	
	public Student getStudent(String user_id) {
		return (Student) this.the_users.get(user_id);
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
