package com.chico.esiuclm.melti.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.model.Solution;
import com.chico.esiuclm.melti.model.Student;

public class DAOTeacher {
	
	// Recoge las soluciones del curso/tarea activos
	public static void getSolutionsDB(String task_id, String course_id) throws ClassNotFoundException, SQLException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call getSolutions(?,?)}");
		cs.setString(1, task_id);
		cs.setString(2, course_id);
		ResultSet rs = cs.executeQuery();
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		
		while(rs.next()) {
			Solution aux = new Solution(rs.getString(1), task_id, course_id, 
					rs.getString(2), rs.getDouble(3), rs.getString(4));
			solutions.add(aux);
		}
		
		MeltiServer.get().getActiveCourse().setCourseSolutions(solutions); // Las guardamos en el curso activo
		db.close();
	}

	// Recoge los estudiantes del curso activo
	public static void getStudentsDB(String course_id) throws SQLException, ClassNotFoundException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call getStudents(?)}");
		cs.setString(1, course_id);
		ResultSet rs = cs.executeQuery();
		ArrayList<Student> students = new ArrayList<Student>();
		
		while(rs.next()) {
			Student aux = new Student(rs.getString(1), rs.getString(2), 
					rs.getString(3), rs.getString(4), course_id);
			students.add(aux);
		}
		
		MeltiServer.get().getActiveCourse().setCourseStudents(students); // Los guardamos en el curso activo
		db.close();
	}

	public static void addQualificationDB(Solution solution) throws SQLException, ClassNotFoundException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call updateSolution(?,?,?,?,?,?)}");
		cs.setString(1, solution.getStudentID());
		cs.setString(2, solution.getTaskID());
		cs.setString(3, solution.getCourseID());
		cs.setString(4, solution.getSvdCode());
		cs.setDouble(5, solution.getCalification());
		cs.setString(6, solution.getCComment());
		cs.executeUpdate();
		db.close();				
	}
	
}
