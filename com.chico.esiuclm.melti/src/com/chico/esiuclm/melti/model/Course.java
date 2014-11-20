package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;
import com.chico.esiuclm.melti.persistence.DAOCourse;

public class Course {

	private String id, title, label;
	
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
	
	public void add() throws ClassNotFoundException, SQLException, GenericErrorException {
		DAOCourse.addCourseDB(this);
	}
	
}