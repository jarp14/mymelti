package com.chico.esiuclm.melti.model;

public abstract class User {

	protected String id;
	protected String first_name, last_name;
	protected String email, role;
	private String course_id;
	
	public User(String id, String first, String last, String email, String role, String courseId) {
		this.id = id;
		this.first_name = first;
		this.last_name = last;
		this.email = email;
		this.role = role;
		this.course_id = courseId;
	}
	
	public String getID() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getRole() {
		return role;
	}
	
	public String getCourseID() {
		return course_id;
	}
	
}
