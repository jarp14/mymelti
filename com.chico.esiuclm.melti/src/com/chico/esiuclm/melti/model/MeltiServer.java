package com.chico.esiuclm.melti.model;

import java.sql.SQLException;
import java.util.Hashtable;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;

public class MeltiServer {
	
	private static MeltiServer yo;
	private Hashtable<String, Course> the_courses; // Cursos
	private Hashtable<String, User> the_users; // Usuarios
	private Hashtable<String, Task> the_tasks; // Tareas
	private Hashtable<String, Solution> the_solutions; // Soluciones de los alumnos
	
	private MeltiServer() {}
	
	// singleton
	public static MeltiServer get() {
		if (yo==null)
			yo = new MeltiServer();
		return yo;
	}
	
	public Hashtable<String, User> getUsers() {
		return the_users;
	}
	
	public Student getStudent(String user_id) {
		return (Student) this.the_users.get(user_id);
	}
	
	public Profesor getProfesor(String user_id) {
		return (Profesor) this.the_users.get(user_id);
	}
	
	public Task getTask(String task_id) {
		return this.the_tasks.get(task_id);
	}
	
	public Course getCourse(String course_id) {
		return this.the_courses.get(course_id);
	}
	
	public void addCourse(Course course) throws ClassNotFoundException, SQLException, GenericErrorException {
		if (the_courses==null) {
			this.the_courses = new Hashtable<String, Course>();
		}
		course.add();
		this.the_courses.put(course.getID(), course);	
	}
	
	public void addTask(Task task) throws ClassNotFoundException, SQLException, GenericErrorException {
		if (the_tasks==null) {
			this.the_tasks = new Hashtable<String, Task>();
		}
		task.add();
		this.the_tasks.put(task.getID(), task);
	}
	
	public void addStudent(Student student) throws ClassNotFoundException, SQLException, GenericErrorException {
		if (the_users==null) {
			this.the_users = new Hashtable<String, User>();
		}
		student.add();
		this.the_users.put(student.getID(), student);
	}

	public void addProfesor(Profesor profesor) throws ClassNotFoundException, SQLException, GenericErrorException {
		if (the_users==null) {
			this.the_users = new Hashtable<String, User>();
		}
		profesor.add();
		this.the_users.put(profesor.getID(), profesor);	
	}
	
}
