package com.chico.esiuclm.melti.model;

public class Task extends ModelObject {
	
	private int id; 
	private String statement, code;
	
	public Task(int id, String st, String co) {
		this. id = id;
		this.statement = st;
		this.code = co;
	}
	
	public int getID() {
		return id;
	}

	public void setID(int id) {
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
	
}
