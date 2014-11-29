package com.chico.esiuclm.melti.net.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;
import com.chico.esiuclm.melti.exceptions.NotCodeException;
import com.chico.esiuclm.melti.exceptions.NotProfesorException;
import com.chico.esiuclm.melti.exceptions.NotStatementException;
import com.chico.esiuclm.melti.exceptions.NotStudentException;
import com.chico.esiuclm.melti.gui.Controller;
import com.chico.esiuclm.melti.net.Proxy;
import com.chico.esiuclm.melti.net.oauth.OAuthException;

@SuppressWarnings("serial")
public class BltiServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {			
		// Informacion de la tarea enviada desde Moodle
		String task_id = request.getParameter("resource_link_id"); // ID de la tarea
		String task_title = request.getParameter("resource_link_title"); // Titulo de la tarea
		String task_statement_code = request.getParameter("resource_link_description"); // Enunciado y codigo
		String task_class_name = request.getParameter("custom_nombre"); // Nombre de la clase
		
		// Informacion del usuario enviada desde Moodle
		String user_id = request.getParameter("user_id"); // ID unico del usuario
		String user_role = request.getParameter("roles"); // Rol del usuario usando el sistema
		String user_email = request.getParameter("lis_person_contact_email_primary"); // E-mail del usuario
		String user_firstName = request.getParameter("lis_person_name_given"); // Nombre del usuario
		String user_lastName = request.getParameter("lis_person_name_family"); // Apellidos del usuario
		
		// Informacion del curso enviada desde Moodle
		String course_id = request.getParameter("context_id"); // ID del curso
		String course_title = request.getParameter("context_title"); // Titulo del curso
		String course_label = request.getParameter("context_label"); // Etiqueta del curso
		
		// Informacion para posible retorno y calificaciones
		//String service_url = request.getParameter("lis_outcome_service_url"); // Direccion de retorno
		//String sourceid = request.getParameter("lis_result_sourceid"); // Datos para retorno
		
		/** 
		 * Comprobaciones de seguridad entre sistemas
		 */		
		// Validacion de credenciales (Key+Secret)
		try {
			Proxy.get().checkOauthCredentials(request); // Comprueba las claves mediante OAuth
			Proxy.get().checkUser(user_email, user_role); // Comprueba credenciales del usuario y las preferencias en Eclipse
		} catch (OAuthException | URISyntaxException | 
				NotProfesorException | NotStudentException | GenericErrorException e1) {
			e1.printStackTrace();
			doError(request, response, 0);
			return;
		}
		
		// Capturamos la tarea desde moodle
		String task_statement;
		String task_code;
		try {
			task_statement = Proxy.get().acquireStatement(task_statement_code); // Adquiere el enunciado (no puede ser nulo) de la tarea
			task_code = Proxy.get().acquireCode(task_statement_code); // Adquiere el codigo de la tarea (puede serlo aunque avisa en caso de error)
		} catch (NotStatementException e3) {
			e3.printStackTrace();
			doError(request, response, 1);
			return;
		} catch (NotCodeException e4) {
			e4.printStackTrace();
			doError(request, response, 2);
			return;
		}
		
		// Todas las comprobaciones resultaron exitosas
		Proxy.get().sayOK(response);
		
		/**
		 * Tras las comprobaciones exitosas...
		 * Generacion de objetos para su manipulacion en la sesion
		 */
		String tclass_name = Proxy.get().checkFileName(task_class_name);
		Proxy.get().addCourseToDB(course_id, course_title, course_label); // Agregamos el curso a la BBDD si aun no esta
		Proxy.get().addTaskToDB(task_id, task_title, tclass_name, task_statement, task_code, course_id); // Agregamos la tarea a la BBDD si aun no esta
		
		if (user_role.equals("Instructor")) { // Si el cliente es un Profesor
			Proxy.get().setActiveProfesor(user_id, user_firstName, user_lastName, user_email, null, course_id);
			Controller.get().updateSolutionsView(task_id, course_id); // Recibe las soluciones de ese contexto, actualiza la vista
		}
		else if (user_role.equals("Learner")) { // Si el cliente es un Estudiante
			// Lo anadimos a la BD si no esta todavia
			Proxy.get().addStudentToDB(user_id, user_firstName, user_lastName, user_email, user_role, course_id);
			try { // Creamos proyecto Java con la tarea
				boolean projectExists = Proxy.get().createProject(task_title, user_firstName+user_lastName, task_code, 
						tclass_name, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Controller.get().updateTaskForView(task_statement); // Envia la llamada para actualizar la vista enunciado
	}
	
	public void doError(HttpServletRequest request, HttpServletResponse response, int errorkey) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<center>");
		switch(errorkey) {
			case 0:
				out.println("<h1>Validaci�n de credenciales incorrecta</h1>\n");
				out.println("<h3>:(</h3>\n");
				out.println("<pre><i>Consulte con su profesor</i></pre>");
				out.println("</center>");
				out.println("</body>");
				out.println("</html>");
				break;
			case 1:
				out.println("<h1>Error inesperado</h1>\n");
				out.println("<h3>No se encuentra el enunciado</h3>\n");
				out.println("<pre><i>Consulte con su profesor</i></pre>");
				out.println("</center>");
				out.println("</body>");
				out.println("</html>");
				break;
				
			case 2: 
				out.println("<h1>Error inesperado</h1>\n");
				out.println("<h3>No se encuentra el c�digo del problema</h3>\n");
				out.println("<pre><i>Consulte con su profesor</i></pre>");
				out.println("</center>");
				out.println("</body>");
				out.println("</html>");
				break;
				
			default:
				out.println("<h1>Error inesperado</h1>\n");
				out.println("<h3>Error desconocido</h3>\n");
				out.println("<pre><i>Consulte con su profesor</i></pre>");
				out.println("</center>");
				out.println("</body>");
				out.println("</html>");
				break;
		}
	}
	
	public void destroy() {}
	
}
