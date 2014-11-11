package com.chico.esiuclm.melti.model;

public class Student extends User {
	
	private Task the_task;

	public Student(String id, String first, String last, String email, String role, Task task) {
		super(id, first, last, email, role);
		this.the_task = task;
	}
	
	public Task getTask() {
		return the_task;
	}
	
	public void setTask(Task task) {
		this.the_task = task;
	}

	/* public boolean isStudent(Student o) {
		boolean isStudent = false;
		if (o instanceof Student && 
				((Student)o).getRole().equals("Student"))
			isStudent = true;
		return isStudent;
	} */
	
	/* LLAMADAS A ACCIONES PROPIAS DE UN ALUMNO */
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof Student && 
				((Student)o).getEmail().equals(this.email) &&
				((Student)o).getRole().equals(this.role));
	}
}
