package com.chico.esiuclm.melti.model;


public abstract class User extends ModelObject {

	protected int id;
	protected String first_name, last_name;
	protected String email, role;
	
	public User(int id, String first, String last, String email, String role) {
		this.id = id;
		this.first_name = first;
		this.last_name = last;
		this.email = email;
		this.role = role;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
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
	
	public String toString() {
		return this.first_name+" "+this.last_name+" : "+this.email;
	}
	
	public boolean equals (Object o) {
		return (o instanceof User && 
				((User)o).email.equals(this.email));
	}
	
}
