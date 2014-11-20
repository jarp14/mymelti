package com.chico.esiuclm.melti.gui;

import com.chico.esiuclm.melti.model.Course;
import com.chico.esiuclm.melti.model.Profesor;
import com.chico.esiuclm.melti.model.Student;
import com.chico.esiuclm.melti.model.Task;

public class Controller {
	private static Controller yo;
	private final Task active_task; // Instancia de tarea mostrada en la vista StatementView
	private Student active_student; // Estudiante haciendo uso del plugin
	private Profesor active_profesor; // Profesor haciendo uso del plugin
	private Course active_course; // Curso activo
	
	public Controller() { // Valores iniciales
		active_task = new Task("-9999", "Enunciado del problema a resolver", " ");
		active_student = new Student("-9999","","","", "");
		active_profesor = new Profesor("-9999","","","","", "");
	}
	
	public static Controller get() {
		if (yo==null) {
			yo = new Controller();
		}
		return yo;
	}
	
	public Task getActiveTask() {
		return this.active_task;
	}
	
	public Profesor getActiveProfesor() {
		return this.active_profesor;
	}
	
	public Student getActiveStudent() {
		return this.active_student;
	}
	
	public Course getActiveCourse() {
		return this.active_course;
	}
	
	public void updateTaskForView(String task_id, String task_statement, String task_code) { // Envia la llamada a la vista para refrescar
		this.active_task.setID(task_id);
		this.active_task.setStatement(task_statement);
		this.active_task.setCode(task_code);
	}
	
}
