package com.chico.esiuclm.melti.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;
import com.chico.esiuclm.melti.model.Student;

public class DAOStudent {

	public static void addStudentDB(Student the_student) throws ClassNotFoundException, SQLException, GenericErrorException {
		Broker broker = Broker.get();
		Connection db = broker.getBD();
		CallableStatement cs = db.prepareCall("{call addStudent(?,?,?,?,?)}");
		cs.setInt(1, the_student.getID());
		cs.setString(2, the_student.getFirst_name());
		cs.setString(3, the_student.getLast_name());
		cs.setString(4, the_student.getEmail());
		cs.registerOutParameter(5, java.sql.Types.BOOLEAN);
		cs.executeUpdate();
		if (cs.getBoolean(5)) throw new GenericErrorException();
		db.close();
	}
	
}
