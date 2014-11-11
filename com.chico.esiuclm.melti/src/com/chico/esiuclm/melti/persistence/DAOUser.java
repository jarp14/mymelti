package com.chico.esiuclm.melti.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.chico.esiuclm.melti.model.User;

public class DAOUser {
	public static void insertarJugadorBD(User user) throws ClassNotFoundException, SQLException {
		Broker broker=Broker.get();
		Connection bd=broker.getBD();
		CallableStatement cs=bd.prepareCall("{call insertarJugador(?,?,?)}");
		cs.setString(1, user.getEmail());
		//cs.setString(2, user.getPassword());
		cs.registerOutParameter(3, java.sql.Types.BOOLEAN);
		cs.executeUpdate();
		//if (!cs.getBoolean(3)) throw new ErrorGenericoException();
		bd.close();
	}

	public static void addUserDB(User user) {
		// TODO Auto-generated method stub
		
	}

}