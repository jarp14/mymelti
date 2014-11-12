package com.chico.esiuclm.melti.net.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.chico.esiuclm.melti.exceptions.GenericErrorException;
import com.chico.esiuclm.melti.exceptions.NotCodeException;
import com.chico.esiuclm.melti.exceptions.NotStatementException;
import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.model.Student;
import com.chico.esiuclm.melti.net.oauth.OAuthAccessor;
import com.chico.esiuclm.melti.net.oauth.OAuthConsumer;
import com.chico.esiuclm.melti.net.oauth.OAuthMessage;
import com.chico.esiuclm.melti.net.oauth.OAuthValidator;
import com.chico.esiuclm.melti.net.oauth.SimpleOAuthValidator;
import com.chico.esiuclm.melti.net.oauth.server.OAuthServlet;

@SuppressWarnings("serial")
public class BltiServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {			
		/**
		 *  Informacion de la tarea enviada desde Moodle
		 */
		String task_id = request.getParameter("resource_link_id"); // ID del contexto de la tarea
		String task_title = request.getParameter("resource_link_title"); // Titulo de la tarea
		String task_statement_code = request.getParameter("resource_link_description"); // Enunciado y codigo
		String task_class_name = request.getParameter("custom_nombre"); // Nombre de la clase
		
		/**
		 *  Informacion del usuario enviada desde Moodle
		 */
		String user_id = request.getParameter("user_id"); // ID unico del usuario
		String user_role = request.getParameter("roles"); // Rol del usuario usando el sistema
		String user_email = request.getParameter("lis_person_contact_email_primary"); // E-mail del usuario
		String user_firstName = request.getParameter("lis_person_name_given"); // Nombre del usuario
		String user_lastName = request.getParameter("lis_person_name_family"); // Apellidos del usuario
		
		/**
		 *  Informacion del curso enviada desde Moodle
		 */
		String course_id = request.getParameter("context_id"); // ID del curso
		String course_title = request.getParameter("context_title"); // Titulo del curso
		String course_label = request.getParameter("context_label"); // Etiqueta del curso
		
		/**
		 * Informacion para posible retorno enviada desde Moodle
		 */
		String service_url = request.getParameter("lis_outcome_service_url"); // Direccion de retorno
		String sourceid = request.getParameter("lis_result_sourceid"); // Datos para retorno
		
		Student a = new Student(Integer.parseInt(user_id), user_firstName, user_lastName, user_email, user_role, null);
		try {
			a.add();
		} catch (ClassNotFoundException | SQLException | GenericErrorException e1) {
			e1.printStackTrace();
		}
		
		/** 
		 * Comprobacion de la clave secreta
		 */
		OAuthMessage oam = OAuthServlet.getMessage(request, null); // Recoge los valores de la peticion
		OAuthValidator oav = new SimpleOAuthValidator();
		OAuthConsumer cons = new OAuthConsumer("about:blank#OAuth+CallBack+NotUsed", "java", "chico.esiuclm.melti", null);
		OAuthAccessor acc = new OAuthAccessor(cons);
		try {
			oav.validateMessage(oam, acc);
		} catch(Exception e) {
			doError(request, response, 0);
			return;
		}
		
		//MeltiServer.get().createUser(user_id, lis_person_name_given, lis_person_name_family, lis_person_contact_email_primary, userrole);
		//MeltiServer.get().createUser(user_id, lis_person_name_given, lis_person_name_family, service_url, sourceid);
		
		// Capturamos la tarea desde moodle
		final String task_statement;
		final String task_code;
		try {
			task_statement = acquireStatement(task_statement_code);
			task_code = acquireCode(task_statement_code);
		} catch (NotStatementException e) {
			doError(request, response, 1);
			return;
		} catch (NotCodeException e) {
			doError(request, response, 2);
			return;
		}
		
		// Enviar enunciado y usuarios a sus vistas y mostrarlas
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("com.chico.esiuclm.melti.views.statementView");
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("com.chico.esiuclm.melti.views.userView");
					//MeltiServer.get().getUser(user_id).getTask().setStatement(statement);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
		
		// Creamos proyecto Java
		try {
			createProject(task_title, task_code, checkFileName(task_class_name));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Todo fue ok
		sayOK(response);
	}
	
	public void doError(HttpServletRequest request, HttpServletResponse response, int errorkey) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<center>");
		switch(errorkey) {
			case 0:
				out.println("<h1>Validación de credenciales incorrecta</h1>\n");
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
				out.println("<h3>No se encuentra el código del problema</h3>\n");
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
	
	private void createProject(String projectName, String fileCode, String fileName) throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	    IProject project = root.getProject(projectName);
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
	    	IPackageFragment fragment = srcFolder.createPackageFragment("(default package)", true, progressMonitor);
	     
	    	StringBuffer buffer = new StringBuffer();
	    	buffer.append("package "+fragment.getElementName()+";\n");
	    	buffer.append(fileCode);
	    	
	    	fragment.createCompilationUnit(fileName, buffer.toString(), false, progressMonitor);
	    }
	}
	
	private String acquireStatement(String st_code) throws NotStatementException {
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
	
	private String acquireCode(String st_code) throws NotCodeException {
		String code;
		String [] c = st_code.split("_[Ff][Ii][Nn]_");
		if (c[1]!=null || !c[1].equals("")) {
			code = c[1];
		} else throw new NotCodeException();
		
		return HTMLUtil.textFromHTML(code);
	}
	
	private String checkFileName(String fileName) {
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
	
	private void sayOK(HttpServletResponse response) throws IOException {
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
	
	public void destroy() {}
	
}
