package com.chico.esiuclm.melti.model;

import java.io.File;

public class Task extends ModelObject {
	
	private String id, statement;
	private File code;
	
	public Task( String string, Object object2) {
		this.id = null;
		this.statement = null;
		this.code = null;
	}
	
	public Task(String id, String st, File co) {
		this. id = id;
		this.statement = st;
		this.code = co;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatement() {
		return statement;
	}
	
	public void setStatement(String s) {
		firePropertyChange("statement", this.statement, this.statement = s);
	}
	
	public File getCode() {
		return code;
	}
	
	public void setCode(File c) {
		this.code = c;
	}
	
	public boolean equals(Object o) {
		return (o instanceof Task && 
				((Task)o).getStatement().equals(this.statement));
	}
	
	public String toString() {
		return "Task";
	}
	
}
