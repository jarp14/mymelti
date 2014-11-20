package com.chico.esiuclm.melti.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;
import com.chico.esiuclm.melti.model.Task;

public class DAOTask {
	
	public static void addTaskDB(Task the_task) throws ClassNotFoundException, SQLException, GenericErrorException {
		Broker broker = Broker.get();
		Connection db = broker.getDB();
		CallableStatement cs = db.prepareCall("{call addTask(?,?,?)}");
		cs.setString(1, the_task.getID());
		cs.setString(2, the_task.getStatement());
		cs.setString(3, the_task.getCode());
		cs.executeUpdate();
		db.close();
	}
	
}
