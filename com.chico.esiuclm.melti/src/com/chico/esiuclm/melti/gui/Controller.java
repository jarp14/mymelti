package com.chico.esiuclm.melti.gui;

import java.util.ArrayList;

import com.chico.esiuclm.melti.exceptions.StudentNotLoggedException;
import com.chico.esiuclm.melti.exceptions.TeacherNotLoggedException;
import com.chico.esiuclm.melti.gui.views.WrappedSolution;
import com.chico.esiuclm.melti.model.Course;
import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.model.Solution;
import com.chico.esiuclm.melti.model.Student;
import com.chico.esiuclm.melti.model.Task;
import com.chico.esiuclm.melti.net.Proxy;

public class Controller {
	private static Controller yo;
	private Task active_task; // Instancia de tarea mostrada en la vista StatementView
	private ArrayList<WrappedSolution> wrapped_solutions; // Array con las soluciones para las vistas SolutionsView y MyTasksView
	private WrappedSolution active_wsolution;
	
	public Controller() { // Valores iniciales
		active_task = new Task("Enunciado del problema a resolver");
		wrapped_solutions = new ArrayList<WrappedSolution>();
	}
	
	// Singleton
	public static Controller get() {
		if (yo==null) {
			yo = new Controller();
		}
		return yo;
	}
	
	// Actualiza la vista Enunciado con el enunciado de la tarea activa
	public void updateTaskView(String task_statement) { // Realiza la llamada a la vista para refrescar
		active_task.setStatement(task_statement);
	}

	// Actualiza la vista soluciones con las soluciones de un contexto curso/tarea
	public void updateSolutionsView(String task_id, String course_id) {
		try {
			Proxy.get().getSolutionsDB(task_id, course_id); // Recoge las soluciones de ese contexto de la BBDD
			Proxy.get().getStudentsDB(course_id); // Recoge los estudiantes de ese contexto de la BBDD
		} catch (TeacherNotLoggedException | StudentNotLoggedException e) {
			e.printStackTrace();
		} 
		
		Course course = MeltiServer.get().getActiveCourse(); // Recoge la informacion del curso
		Task task = MeltiServer.get().getActiveTask(); // Recoge la informacion de la tarea
		ArrayList<Student> course_students = MeltiServer.get().getActiveCourse().getCourseStudents(); // Recoge los estudiantes de ese curso
		ArrayList<Solution> solutions = MeltiServer.get().getActiveCourse().getCourseSolutions(); // Recoge las soluciones de esa tarea
		
		WrappedSolution aux;
		Student st;
		ArrayList<WrappedSolution> wsolutions = new ArrayList<WrappedSolution>();
		for(int i=0; i<solutions.size(); i++) {
			for(int j=0; j<course_students.size(); j++) {
				if (solutions.get(i).getStudentID().equals(course_students.get(j).getID())) {
					st = course_students.get(j);
					aux = new WrappedSolution(solutions.get(i),
							course.getTitle(),
							task.getTitle(),
							task.getTaskClassName(),
							st.getFirst_name()+" "+st.getLast_name(), 
							st.getEmail());
					wsolutions.add(aux);
				}
			}
		}
		wrapped_solutions = wsolutions; // Actualizamos la informacion a mostrar en la vista
	}
	
	// Actualiza la vista de las tareas de un alumno
	public void updateStudentTasksView(String user_id) {
		try {
			Proxy.get().getMySolutionsDB(user_id); // Recoge las soluciones asociadas al alumno (actualiza su listado)
			Proxy.get().getMyTasksDB(user_id); // Recoge las tareas asociadas del alumno (actualiza su listado)
			Proxy.get().getMyCoursesDB(user_id); // Recoge la informacion de los cursos asociados al alumno (actualiza su listado)
		} catch (StudentNotLoggedException | TeacherNotLoggedException e) {
			
		}
		
		Course course = null; // Recoge la informacion del curso
		Task task = null; // Recoge la informacion de la tarea
		Student student = MeltiServer.get().getActiveStudent();
		ArrayList<Task> student_tasks = MeltiServer.get().getActiveStudent().getMyTasks();
		ArrayList<Course> student_courses = MeltiServer.get().getActiveStudent().getMyCourses();
		ArrayList<Solution> student_solutions = MeltiServer.get().getActiveStudent().getMySolutions(); // Recoge las soluciones de esa alumno
		
		WrappedSolution aux = null;
		ArrayList<WrappedSolution> wsolutions = new ArrayList<WrappedSolution>();
		for(int i=0; i<student_solutions.size(); i++) {
			for(int j=0; j<student_tasks.size(); j++) {
				for(int k=0; k<student_courses.size(); k++) {
					if(student_solutions.get(i).getTaskID().equals(student_tasks.get(j).getID())
							&& student_solutions.get(i).getCourseID().equals(student_courses.get(k).getID())) {
						task = student_tasks.get(j);
						course = student_courses.get(k);
						aux = new WrappedSolution(student_solutions.get(i),
								course.getTitle(),
								task.getTitle(),
								task.getTitle().replace(" ", ""),
								student.getFirst_name()+" "+student.getLast_name(),
								student.getEmail());
						wsolutions.add(aux);
					}
				}
			}
		}
		wrapped_solutions = wsolutions; // Actualizamos la informacion a mostrar en la vista
	}
	
	/**
	 * Getters Setters
	 */
	public Task getActiveTask() {
		return active_task;
	}
	public ArrayList<WrappedSolution> getWrappedSolutions() {
		return wrapped_solutions;
	}
	public WrappedSolution getActiveWrappedSolution() {
		return active_wsolution;
	}
	public void setActiveWrappedSolution(WrappedSolution ws) {
		active_wsolution = ws;
	}
	
}
