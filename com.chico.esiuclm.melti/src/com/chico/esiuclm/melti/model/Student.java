package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;
import com.chico.esiuclm.melti.persistence.DAOStudent;

public class Student extends User {
	
	private Task the_task;
	private boolean task_finished;

	public Student(int id, String first, String last, String email, String role, Task task) {
		super(id, first, last, email, role);
		this.the_task = task;
		this.task_finished = false;
	}
	
	public Task getTask() {
		return the_task;
	}
	
	public void setTask(Task task) {
		this.the_task = task;
	}
	
	public void add() throws ClassNotFoundException, SQLException, GenericErrorException {
		DAOStudent.addStudentDB(this);
	}

}
