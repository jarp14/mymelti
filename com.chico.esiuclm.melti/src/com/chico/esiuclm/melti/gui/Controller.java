package com.chico.esiuclm.melti.gui;

import com.chico.esiuclm.melti.model.Task;

public class Controller {
	private static Controller yo;
	private Task active_task; // Instancia de tarea mostrada en la vista StatementView
	
	public Controller() { // Valores iniciales
		active_task = new Task("Enunciado del problema a resolver");
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
	
	public void updateTaskForView(String task_statement) { // Envia la llamada a la vista para refrescar
		this.active_task.setStatement(task_statement);
	}
	
}
