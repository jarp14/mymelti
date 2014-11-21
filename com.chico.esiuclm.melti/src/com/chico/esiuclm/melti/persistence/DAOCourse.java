package com.chico.esiuclm.melti.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;
import com.chico.esiuclm.melti.model.Course;

public class DAOCourse {

	public static void addCourseDB(Course the_course) throws ClassNotFoundException, SQLException, GenericErrorException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call addCourse(?,?,?)}");
		cs.setString(1, the_course.getID());
		cs.setString(2, the_course.getTitle());
		cs.setString(3, the_course.getLabel());
		cs.executeUpdate();
		db.close();
	}
	
	public static void removeCourseDB(Course the_course) {
		
	}
	
}
