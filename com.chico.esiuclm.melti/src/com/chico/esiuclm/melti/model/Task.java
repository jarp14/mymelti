package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.persistence.DAOTask;

public class Task extends ModelObject {
	
	private String id; 
	private String statement, code;
	private String courseID;
	
	public Task(String st) {
		this.statement = st;
	}
	
	public Task(String id, String st, String co, String cId) {
		this.id = id;
		this.statement = st;
		this.code = co;
		this.courseID = cId;
	}
	
	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getStatement() {
		return statement;
	}
	
	public void setStatement(String s) { // Lanza una llamada para la vista StatementView
		firePropertyChange("statement", this.statement, this.statement = s);
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCourseID() {
		return this.courseID;
	}
	
	public void add() throws ClassNotFoundException, SQLException {
		DAOTask.addTaskDB(this);
	}

	
	
}
