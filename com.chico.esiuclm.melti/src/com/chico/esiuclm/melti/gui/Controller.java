package com.chico.esiuclm.melti.gui;

import java.util.ArrayList;

import com.chico.esiuclm.melti.exceptions.UserNotLoggedException;
import com.chico.esiuclm.melti.model.Course;
import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.model.Solution;
import com.chico.esiuclm.melti.model.Student;
import com.chico.esiuclm.melti.model.Task;
import com.chico.esiuclm.melti.net.Proxy;

public class Controller {
	private static Controller yo;
	private Task active_task; // Instancia de tarea mostrada en la vista StatementView
	private ArrayList<Student> students_with_solutions;
	
	public Controller() { // Valores iniciales
		active_task = new Task("Enunciado del problema a resolver");
		students_with_solutions = new ArrayList<Student>();
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

	public void updateSolutionsView(String task_id, String course_id) {
		prepareSolutionsView(task_id, course_id);
		ArrayList<Solution> solutions = MeltiServer.get().getActiveCourse().getCourseSolutions(); // Recoge las soluciones de esa tarea
		getStudentsWithSolutions(MeltiServer.get().getActiveCourse().getCourseStudents(), solutions);
		Task task = MeltiServer.get().getActiveTask(); // Recoge la informacion de esa tarea
		Course course = MeltiServer.get().getActiveCourse();
	}
	
	private void prepareSolutionsView(String task_id, String course_id) {
		try {
			Proxy.get().getSolutionsDB(task_id, course_id);
			Proxy.get().getStudentsDB(course_id);
		} catch (UserNotLoggedException e) {
			e.printStackTrace();
		}
	}
	
	private void getStudentsWithSolutions(ArrayList<Student> course_students, ArrayList<Solution> course_solutions) {
		String course_student_id, student_id;
		
		for (int i=0; i<course_students.size(); i++) {
			student_id = course_students.get(i).getID();
			for (int j=0; j<course_solutions.size(); j++) {
				course_student_id = course_solutions.get(j).getStudentID();
				if (course_student_id.equals(student_id)) {
					students_with_solutions.add(course_students.get(i));
				}
			}
		}
	}

	public ArrayList<Student> getStudentsWithSolution() {
		return this.students_with_solutions;
	}
	
}
