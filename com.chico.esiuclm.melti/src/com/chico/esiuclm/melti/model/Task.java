package com.chico.esiuclm.melti.model;

public class Task extends ModelObject {
	
	private int id; 
	private String statement, code;
	private String grade;
	
	public Task(int id, String st, String co) {
		this. id = id;
		this.statement = st;
		this.code = co;
		this.grade = null;
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
	
	public void setStatement(String s) {
		firePropertyChange("statement", this.statement, this.statement = s);
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public boolean equals(Object o) {
		return (o instanceof Task && 
				((Task)o).getStatement().equals(this.statement));
	}
	
	public String toString() {
		return "Task "+this.getID();
	}
	
}
