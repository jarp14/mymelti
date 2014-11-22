package com.chico.esiuclm.melti.model;


public abstract class User extends ModelObject {

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
	
	public void setID(String id) {
		firePropertyChange("user_id", this.id, this.id = id);
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getCourseID() {
		return this.course_id;
	}
	
}
