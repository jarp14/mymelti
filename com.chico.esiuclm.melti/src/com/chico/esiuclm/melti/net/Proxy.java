package com.chico.esiuclm.melti.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.clapper.util.html.HTMLUtil;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.chico.esiuclm.melti.MeltiPlugin;
import com.chico.esiuclm.melti.exceptions.GenericErrorException;
import com.chico.esiuclm.melti.exceptions.NotCodeException;
import com.chico.esiuclm.melti.exceptions.NotProfesorException;
import com.chico.esiuclm.melti.exceptions.NotStatementException;
import com.chico.esiuclm.melti.exceptions.NotStudentException;
import com.chico.esiuclm.melti.model.Course;
import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.model.Profesor;
import com.chico.esiuclm.melti.model.Student;
import com.chico.esiuclm.melti.model.Task;
import com.chico.esiuclm.melti.net.oauth.OAuthAccessor;
import com.chico.esiuclm.melti.net.oauth.OAuthConsumer;
import com.chico.esiuclm.melti.net.oauth.OAuthException;
import com.chico.esiuclm.melti.net.oauth.OAuthMessage;
import com.chico.esiuclm.melti.net.oauth.OAuthValidator;
import com.chico.esiuclm.melti.net.oauth.SimpleOAuthValidator;
import com.chico.esiuclm.melti.net.oauth.server.OAuthServlet;
import com.chico.esiuclm.melti.net.servlets.BltiServlet;

/**
 * Proxy encargado de realizar comprobaciones de seguridad
 * y creacion de objetos. Ahorro de recursos siempre que es
 * posible
 */

public class Proxy {

	private static Proxy yo; // Instancia del proxy
	private Server jettyserver; // Contenedor de servlets
	private MeltiServer server; // Manejador de objetos de Melti
	
	public Proxy() {
		initJettyServer();
		this.server = MeltiServer.get();
	}
	
	// Singleton
	public static Proxy get() {
		if (yo==null) {
			yo = new Proxy();
		}
		return yo;
	}
	
	/**
	 * Metodos del contenedor de servlets Jetty
	 */
	// Obtiene el contenedor de servlets
	public Server getJettyServer() {
		return jettyserver;
	}
	
	// Inicializa el contenedor de servlets
	private void initJettyServer() {
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
	}
	
	/**
	 * Trabajando con la BBDD y generacion de objetos en el sistema
	 */
	// Agregar un estudiante a la BBDD
	public void addStudentToDB(String id, String first, String last, String email, String role, String courseId) {
		Student student = new Student(id, first, last, email, role, courseId);
		try {
			this.server.addStudent(student);
			this.server.setActiveStudent(student); // Valores del estudiante activo
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (GenericErrorException e) {
			e.printStackTrace();
		}
	}
	
	// Agregar una tarea
	public void addTaskToDB(String id, String statement, String code, String courseId) {
		Task task = new Task(id, statement, code, courseId);
		try {
			this.server.addTask(task);
			this.server.setActiveTask(task); // Valores de la tarea activa
		} catch (ClassNotFoundException | SQLException | GenericErrorException e) {
			e.printStackTrace();
		}
	}
	
	// Agregar un curso
	public void addCourseToDB(String id, String title, String label) {
		Course course = new Course(id, title, label);
		try {
			this.server.addCourse(course);
			this.server.setActiveCourse(course); // Valores del curso activo
		} catch (ClassNotFoundException | SQLException | GenericErrorException e) {
			e.printStackTrace();
		}
	}
	
	// Guardamos el profesor usando el sistema
	public void setActiveProfesor(String id, String first, String last, String email, String role, String courseId) {
		Profesor profesor = new Profesor(id, first, last, email, role, null, courseId);
		this.server.setActiveProfesor(profesor); // Valores del profesor activo
	}
	
	/**
	 * Metodos para asegurar la identidad del usuario
	 * asi como la recepcion y correcta creacion de una tarea
	 */
	// Metodo para capturar y limpiar el enunciado enviado desde Moodle
	public String acquireStatement(String st_code) throws NotStatementException {
		String statement;
		Pattern p = Pattern.compile("_[Ii][Nn][Ii][Cc][Ii][Oo]_.*_[Ff][Ii][Nn]_", Pattern.DOTALL);
		Matcher m = p.matcher(st_code);
		boolean find = m.find(); // contiene
		if (find) {
			statement = m.group(0);
			String [] st = statement.split("_[Ii][Nn][Ii][Cc][Ii][Oo]_");
			st = st[1].split("_[Ff][Ii][Nn]_");
			statement = st[0];
		} else throw new NotStatementException();			
		
		return HTMLUtil.textFromHTML(statement);
	}
	
	// Metodo para capturar y limpiar el codigo enviado desde Moodle
	public String acquireCode(String st_code) throws NotCodeException {
		String code;
		String [] c = st_code.split("_[Ff][Ii][Nn]_");
		if (c[1]!=null || !c[1].equals("")) {
			code = c[1];
		} else throw new NotCodeException();
		
		return HTMLUtil.textFromHTML(code);
	}
	
	// Metodo para capturar o generar el nombre de la clase de la tarea
	public String checkFileName(String fileName) {
		Pattern p = Pattern.compile("[a-zA-Z]*.java");
		if(fileName!=null) {
			Matcher m = p.matcher(fileName);
			boolean matches = m.matches(); // coincide
			if (!matches) {
				p = Pattern.compile("[a-zA-Z]*");
				m = p.matcher(fileName);
				matches = m.matches();
				if (matches) {
					fileName += ".java";
				} else {
					fileName = "tarea.java";
				}
			}
		} else fileName = "tarea.java";
		
		return fileName;
	}
	
	// Metodo encargado de crear el proyecto JAVA en el workspace del usuario
	public void createProject(String projectName, String user_id, String task_id, String fileCode, String fileName) throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	    IProject project = root.getProject(projectName+"_"+user_id+"_"+task_id); // Para asociarlo al usuario
	    IJavaProject javaProject = null;
	    IProgressMonitor progressMonitor = new NullProgressMonitor();
	    if(!project.exists()) {
	    	project.create(progressMonitor);
	    	project.open(progressMonitor);
	 
	    	// Agregamos parametros para Java
	    	IProjectDescription description = project.getDescription(); 	
	    	description.setNatureIds(new String[] { JavaCore.NATURE_ID });
	    
	    	// Generar proyecto Java
	    	project.setDescription(description, progressMonitor);
	    	javaProject = JavaCore.create(project);
	    
	    	// Configurar buildpath
	    	IClasspathEntry[] buildPath = {
	    		JavaCore.newSourceEntry(project.getFullPath().append("src")),
	    				JavaRuntime.getDefaultJREContainerEntry() };
	    	javaProject.setRawClasspath(buildPath, project.getFullPath().append("bin"), progressMonitor);
	     
	    	// Crear carpeta para el código
	    	IFolder folder = project.getFolder("src");
	    	folder.create(true, true, null);
	     
	    	// Crear carpeta para elementos java
	    	IPackageFragmentRoot srcFolder = javaProject.getPackageFragmentRoot(folder);
	     
	    	// Crear fragmento de paquete
	    	IPackageFragment fragment = srcFolder.createPackageFragment(projectName.toLowerCase(), true, progressMonitor);
	     
	    	StringBuffer buffer = new StringBuffer();
	    	buffer.append("package "+fragment.getElementName()+";\n");
	    	buffer.append(fileCode);
	    	
	    	fragment.createCompilationUnit(fileName, buffer.toString(), false, progressMonitor);
	    }
	}
	
	// Comprobaciones relacionadas con las credenciales del usuario y las preferencias en Eclipse
		public void checkUser(String email, String role) throws NotProfesorException, NotStudentException, GenericErrorException {
			String eclipse_userID = MeltiPlugin.getDefault().getPreferenceStore().getString("melti_id"); // ID de las preferencias en Eclipse
			String eclipse_userRole = MeltiPlugin.getDefault().getPreferenceStore().getString("melti_role"); // Rol de las preferencias en Eclipse
		
			if (!email.equals(eclipse_userID)) { // No coinciden el usuario de Moodle y Eclipse
				throw new GenericErrorException();
			}
			
			if (role.equals("Instructor")) {
				if (!eclipse_userRole.equals("melti_rprofesor"))  // No tiene los privilegios de profesor
					throw new NotProfesorException();
			} else if (role.equals("Learner")) { 
				if (!eclipse_userRole.equals("melti_rstudent")) // No tiene los privilegios de estudiante
					throw new NotStudentException();
			} else {
				throw new GenericErrorException();
			}
		}

	// Comprobacion de las credenciales usando OAuth
	public void checkOauthCredentials(HttpServletRequest request) throws OAuthException, IOException, URISyntaxException {
		String eclipse_user_key = MeltiPlugin.getDefault().getPreferenceStore().getString("melti_key"); // Valor obtenido de las preferencias
		String eclipse_user_secret = MeltiPlugin.getDefault().getPreferenceStore().getString("melti_secret"); // Valor obtenido de las preferencias
		OAuthMessage oam = OAuthServlet.getMessage(request, null); // Recoge los valores de la peticion HTTP
		OAuthValidator oav = new SimpleOAuthValidator();
		OAuthConsumer cons = new OAuthConsumer("about:blank#OAuth+CallBack+NotUsed", eclipse_user_key, eclipse_user_secret, null); // Insertamos datos de las preferencias
		OAuthAccessor acc = new OAuthAccessor(cons);
		
		oav.validateMessage(oam, acc); // Validacion de credenciales
	}
	
	// Informamos de que las comprobaciones fueron exitosas
		public void sayOK(HttpServletResponse response) throws IOException {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<center>");
			out.println("<h1>Validación de credenciales correcta</h1>\n");
			out.println("<h3>:)</h3>\n");
			out.println("</center>");
			out.println("</body>");
			out.println("</html>");
		}
	
}
