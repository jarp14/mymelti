package com.chico.esiuclm.melti.model;

import java.sql.SQLException;
import java.util.ArrayList;

import com.chico.esiuclm.melti.persistence.DAOCourse;

public class Course {

	private String id, title, label;
	private ArrayList<Task> the_tasks;
	private ArrayList<Student> the_students;
	private ArrayList<Solution> the_solutions;
	
	public Course(String id, String title, String label) {
		this.id = id;
		this.title = title;
		this.label = label;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public ArrayList<Task> getCourseTasks() {
		if(the_tasks == null)
			the_tasks = new ArrayList<Task>();
		return this.the_tasks;
	}
	
	public void setCourseTasks(ArrayList<Task> tasks) {
		this.the_tasks = tasks;
	}
	
	public ArrayList<Student> getCourseStudents() {
		if(the_students == null)
			the_students = new ArrayList<Student>();
		return this.the_students;
	}
	
	public void setCourseStudents(ArrayList<Student> students) {
		this.the_students = students;
	}
	
	public ArrayList<Solution> getCourseSolutions() {
		if(the_solutions == null)
			the_solutions = new ArrayList<Solution>();
		return this.the_solutions;
	}
	
	public void setCourseSolutions(ArrayList<Solution> solutions) {
		this.the_solutions = solutions;
	}
	
	// Añade este curso a la BBDD
	public void add() throws ClassNotFoundException, SQLException {
		DAOCourse.addCourseDB(this);
	}
	
}