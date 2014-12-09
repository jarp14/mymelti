package com.chico.esiuclm.melti.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.chico.esiuclm.melti.exceptions.SolvedErrorException;
import com.chico.esiuclm.melti.model.Course;
import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.model.Solution;
import com.chico.esiuclm.melti.model.Student;
import com.chico.esiuclm.melti.model.Task;

public class DAOStudent {

	public static void addStudentDB(Student the_student) throws ClassNotFoundException, SQLException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call addStudent(?,?,?,?,?)}");
		cs.setString(1, the_student.getID());
		cs.setString(2, the_student.getFirst_name());
		cs.setString(3, the_student.getLast_name());
		cs.setString(4, the_student.getEmail());
		cs.setString(5, the_student.getCourseID());
		cs.executeUpdate(); // Añade el estudiante a la BBDD
		db.close();
	}

	public static void addSolutionDB(Solution solution) throws ClassNotFoundException, SQLException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call addSolution(?,?,?,?,?,?)}");
		cs.setString(1, solution.getStudentID());
		cs.setString(2, solution.getTaskID());
		cs.setString(3, solution.getCourseID());
		cs.setString(4, solution.getSvdCode());
		cs.setDouble(5, solution.getCalification());
		cs.setString(6, solution.getCComment());
		cs.executeUpdate(); // Añade su solucion a la BBDD
		db.close();
	}

	public static void checkIfSolutionSolvedDB(String[] task_context) throws SQLException, ClassNotFoundException, SolvedErrorException {
		Broker broker=Broker.get();
		Connection bd=broker.getDB();
		CallableStatement cs=bd.prepareCall("{call checkSolution(?,?,?,?)}");
		cs.setString(1, task_context[0]);
		cs.setString(2, task_context[1]);
		cs.setString(3, task_context[2]);
		cs.registerOutParameter(4, java.sql.Types.BOOLEAN);
		cs.executeUpdate();
		// Si esa tarea ya esta resuelta por el alumno
		if (cs.getBoolean(4)) throw new SolvedErrorException(); 
		bd.close();
	}

	public static void getMySolutionsDB(String user_id) throws SQLException, ClassNotFoundException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call getMySolutions(?)}");
		cs.setString(1, user_id);
		ResultSet rs = cs.executeQuery();
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		
		while(rs.next()) {
			Solution aux = new Solution(user_id, rs.getString(1), rs.getString(2),
					rs.getString(3), rs.getDouble(4), rs.getString(5));
			solutions.add(aux);
		}
		
		// Guardamos la informacion de sus soluciones asociadas
		MeltiServer.get().getActiveStudent().setMySolutions(solutions); 
		db.close();
	}

	public static void getMyCoursesDB(String user_id) throws SQLException, ClassNotFoundException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call getMyCourses(?)}");
		cs.setString(1, user_id);
		ResultSet rs = cs.executeQuery();
		ArrayList<Course> courses = new ArrayList<Course>();
		
		while(rs.next()) {
			Course aux = new Course(rs.getString(1), rs.getString(2));
			courses.add(aux);
		}
		
		// Guardamos la informacion de sus cursos asociados
		MeltiServer.get().getActiveStudent().setMyCourses(courses);
		db.close();
	}
	
	public static void getMyTasksDB(String user_id) throws SQLException, ClassNotFoundException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call getMyTasks(?)}");
		cs.setString(1, user_id);
		ResultSet rs = cs.executeQuery();
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		while(rs.next()) {
			Task aux = new Task(rs.getString(1), rs.getString(2), rs.getString(3));
			tasks.add(aux);
		}
		
		// Guardamos la informacion de sus tareas asociadas
		MeltiServer.get().getActiveStudent().setMyTasks(tasks);
		db.close();
	}
	
}
