package com.chico.esiuclm.melti.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.chico.esiuclm.melti.model.Solution;
import com.chico.esiuclm.melti.model.Student;

public class DAOStudent {

	public static void addStudentDB(Student the_student) throws ClassNotFoundException, SQLException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call addStudent(?,?,?,?)}");
		cs.setString(1, the_student.getID());
		cs.setString(2, the_student.getFirst_name());
		cs.setString(3, the_student.getLast_name());
		cs.setString(4, the_student.getEmail());
		cs.executeUpdate();
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
		cs.setDouble(5, -999.0);
		cs.setString(6, "");
		cs.executeUpdate();
		db.close();
	}
	
}
