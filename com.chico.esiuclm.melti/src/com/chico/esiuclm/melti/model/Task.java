package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.persistence.DAOTask;

public class Task extends ModelObject {
	
	private String id; 
	private String statement, code;
	private String course_id;
	private String task_title;
	private String task_className;
	
	// Tarea asociada a la vista Statement
	public Task(String st) {
		this.statement = st;
	}
	
	// Tarea asociada a un alumno
	public Task(String id, String tt, String cn) {
		this.id = id;
		this.task_title = tt;
		this.task_className = cn;
	}
	
	// Toda la informacion relevante de una tarea
	public Task(String id, String tt, String tcn, String st, String co, String cId) {
		this.id = id;
		this.task_title = tt;
		this.task_className = tcn;
		this.statement = st;
		this.code = co;
		this.course_id = cId;
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
	public String getTaskClassName() {
		return this.task_className;
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
		return this.course_id;
	}
	public String getTitle() {
		return this.task_title;
	}
	
	public void add() throws ClassNotFoundException, SQLException {
		DAOTask.addTaskDB(this);
	}
}
