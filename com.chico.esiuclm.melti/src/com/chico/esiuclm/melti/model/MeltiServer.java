package com.chico.esiuclm.melti.model;

import java.util.Hashtable;

public class MeltiServer {
	
	private static MeltiServer yo;
	private Student the_student;
	private Hashtable<String, Solution> the_solutions; // Soluciones de los alumnos
	private Hashtable<String, Student> the_students; // Estudiantes del curso
	
	private MeltiServer() {
		the_students = new Hashtable<String, Student>();
	}
	
	public static MeltiServer get() {
		if (yo==null)
			yo = new MeltiServer();
		return yo;
	}
	
	
	public Hashtable<String, Student> getStudents() {
		return the_students;
	}
	
	public Student getStudent(String user_id) {
		return this.the_students.get(user_id);
	}
	
	public void registrar(Student student) {
		
	}
	
}
