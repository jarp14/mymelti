package com.chico.esiuclm.melti.model;

import java.util.Hashtable;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.chico.esiuclm.melti.net.servlets.BltiServlet;

public class MeltiServer {
	
	private static MeltiServer yo; // Instancia del servidor
	private Server jettyserver; // Contenedor de Servlets
	private Course the_course; // Curso accedido
	private Hashtable<String, Student> the_students; // Estudiantes del curso
	private Hashtable<String, Profesor> the_profesors; // Profesores del curso
	private Hashtable<String, Task> the_tasks; // Tareas del curso
	//private User [] user;
	
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
	
	public Hashtable<String, Student> getStudents() {
		return the_students;
	}
	
	public Hashtable<String, Task> getTasks() {
		return the_tasks;
	}
	
	public Student getStudent(String user_id) {
		return this.the_students.get(user_id);
	}
	
	public Profesor getProfesor(String user_id) {
		return this.the_profesors.get(user_id);
	}
	
	public Task getTask(String task_id) {
		return this.the_tasks.get(task_id);
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
