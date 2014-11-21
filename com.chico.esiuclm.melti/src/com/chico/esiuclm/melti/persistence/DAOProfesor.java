package com.chico.esiuclm.melti.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;
import com.chico.esiuclm.melti.model.Profesor;
import com.chico.esiuclm.melti.model.Student;

public class DAOProfesor {
	public static void addProfesorDB(Profesor the_profesor) throws ClassNotFoundException, SQLException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call addProfesor(?,?,?,?)}");
		cs.setString(1, the_profesor.getID());
		cs.setString(2, the_profesor.getFirst_name());
		cs.setString(3, the_profesor.getLast_name());
		cs.setString(4, the_profesor.getEmail());
		cs.executeUpdate();
		db.close();
	}
	
	public static void removeProfesorDB(Profesor the_profesor) {
		
	}
}
