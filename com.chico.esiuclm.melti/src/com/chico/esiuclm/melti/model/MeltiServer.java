package com.chico.esiuclm.melti.model;

import java.util.Hashtable;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.chico.esiuclm.melti.net.servlets.BltiServlet;

public class MeltiServer {
	
	private static MeltiServer yo;
	private Server jettyserver;
	private Hashtable<String, User> users;
	private Hashtable<String, Task> tasks;
	private Task the_task;
	private User [] user;
	
	public MeltiServer() {
		jettyserver = new Server();
		
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setHost("localhost");
		connector.setPort(8080);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		
		jettyserver.setConnectors(new Connector[]{connector});
		jettyserver.setHandler(context);
		jettyserver.setStopAtShutdown(true);
		
		context.addServlet(new ServletHolder(new BltiServlet()), "/melti/*");
		//context.addServlet(new ServletHolder(new ServiceServlet()), "/service/*");
	
		//the_task = new Task(null, "Enunciado del problema", null);
		//user = new User[]{ new User("","","","","", "") };
	}
	
	public static MeltiServer get() {
		if (yo==null) {
			yo = new MeltiServer();
		}
		return yo;
	}
	
	public Server getServer() {
		return jettyserver;
	}
	
	public Hashtable<String, User> getUsers() {
		return users;
	}
	
	public Hashtable<String, Task> getTasks() {
		return tasks;
	}
	
	public User getUser(String user_id) {
		return this.users.get(user_id);
	}
	
	public Task getTask(String task_id) {
		return this.tasks.get(task_id);
	}
	
	/*public User[] getUser() {
		return this.user;
	}

	public void createUser(String i, String n, String f, String e, String r) {
		if(user!=null) {
			user[0].setEmail(e);
			user[0].setLast_name(f);
			user[0].setID(i);
			user[0].setFirst_name(n);
			user[0].setRole(r);
		}
	}*/
	
	
	
}
