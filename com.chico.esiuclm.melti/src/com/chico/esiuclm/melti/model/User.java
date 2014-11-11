package com.chico.esiuclm.melti.model;

import com.chico.esiuclm.melti.persistence.DAOUser;

public class User extends ModelObject {

	protected String id, email;
	protected String first_name, last_name;
	protected String role;
	
	public User(String id, String first, String last, String email, String role) {
		this.id = id;
		this.first_name = first;
		this.last_name = last;
		this.email = email;
		this.role = role;
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
	
	public void addUserDB(String id) {
		DAOUser.addUserDB(this);
	}
	
	public String toString() {
		return this.first_name+" "+this.last_name+" : "+this.email;
	}
	
	public boolean equals (Object o) {
		return (o instanceof User && 
				((User)o).email.equals(this.email));
	}
	
	/* ACCIONES COMUNES DE UN USUARIO CUALQUIERA */
	
}
