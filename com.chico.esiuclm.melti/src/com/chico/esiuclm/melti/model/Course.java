package com.chico.esiuclm.melti.model;

import java.sql.SQLException;
import java.util.ArrayList;

import com.chico.esiuclm.melti.persistence.DAOCourse;

public class Course {

	private String id, title, label;
	private ArrayList<Task> the_tasks; // Las tareas del curso
	private ArrayList<Student> the_students; // El listado de alumnos del curso
	private ArrayList<Solution> the_solutions; // Guarda las resoluciones del curso
	
	public Course(String id, String ct) {
		this.id = id;
		this.title = ct;
	}
	
	public Course(String id, String title, String label) {
		this.id = id;
		this.title = title;
		this.label = label;
	}
	
	public String getID() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getLabel() {
		return label;
	}
	public ArrayList<Student> getCourseStudents() {
		if (the_students == null)
			the_students = new ArrayList<Student>();
		return the_students;
	}
	public void setCourseStudents(ArrayList<Student> students) {
		the_students = students;
	}
	public ArrayList<Solution> getCourseSolutions() {
		if (the_solutions == null)
			the_solutions = new ArrayList<Solution>();
		return the_solutions;
	}
	public void setCourseSolutions(ArrayList<Solution> solutions) {
		the_solutions = solutions;
	}
	public ArrayList<Task> getCourseTasks() {
		if (the_tasks == null)
			the_tasks = new ArrayList<Task>();
		return the_tasks;
	}
	
	// Añade este curso a la BBDD
	public void add() throws ClassNotFoundException, SQLException {
		DAOCourse.addCourseDB(this);
	}
	
}