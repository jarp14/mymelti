package com.chico.esiuclm.melti.net.servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.chico.esiuclm.melti.exceptions.NotCodeException;
import com.chico.esiuclm.melti.exceptions.NotStatementException;
import com.chico.esiuclm.melti.model.MeltiServer;
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
		// Requeridos
		String statement_code = request.getParameter("resource_link_description");
		String class_name = request.getParameter("custom_nombre");
		String userrole = request.getParameter("roles");
		String lis_person_contact_email_primary = request.getParameter("lis_person_contact_email_primary"); // R. E-mail del usuario
		//String lti_version = request.getParameter("lti_version"); // REQUERIDO. Version LTI: LTI-1p0 (BasicLTI)
		//String lti_message_type = request.getParameter("lti_message_type"); // Tipo de mensaje LTI: basic-lti-launch-request
		String resource_link_id = request.getParameter("resource_link_id"); // REQUERIDO. ID único para la app
		final String user_id = request.getParameter("user_id"); // ID único del usuario, debe considerarse opaco/oculto
		String lis_person_name_full = request.getParameter("lis_person_name_full");
		String lis_person_name_given = request.getParameter("lis_person_name_given");
		String lis_person_name_family = request.getParameter("lis_person_name_family");
		String service_url = request.getParameter("lis_outcome_service_url"); // Enviar notas de vuelta
		String sourceid = request.getParameter("lis_result_sourceid"); // A donde enviar las notas de regreso MUY IMPORTANTE
		String resource_title = request.getParameter("resource_link_title");
		String context_id = request.getParameter("context_id");
		String context_label = request.getParameter("context_label");
		String context_title = request.getParameter("context_title");
		String launch_presentation_locale = request.getParameter("launch_presentation_locale");		
		//String oauth_signature_method = request.getParameter("oauth_signature_method");
		//String launch_presentation_return_url = request.getParameter("launch_presentation_return_url");
		//String oauth_nonce = request.getParameter("oauth_nonce");
		//String oauth_timestamp = request.getParameter("oauth_timestamp");
		//String oauth_signature = request.getParameter("oauth_signature");
		//String oauth_callback = request.getParameter("oauth_callback");	
		String tool_consumer_instance_guid = request.getParameter("tool_consumer_instance_guid");
		
		// Comprobamos la clave secreta
		OAuthMessage oam = OAuthServlet.getMessage(request, null);
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
		final String statement;
		final String code;
		try {
			statement = acquireStatement(statement_code);
			code = acquireCode(statement_code);
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
			createProject(resource_title, code, checkFileName(class_name));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Todo fue ok
		sayOK(response);
		
		/*if (userrole==null) userrole = "";
		userrole = userrole.toLowerCase();
        boolean isInstructor = userrole.indexOf("instructor") >= 0;
        boolean isStudent = userrole.indexOf("student") >= 0;

        // Contextos del usuario/curso en moodle
		String userKey = null;
		if ( user_id != null ) {
			userKey = oauth_consumer_key + ":" + user_id;
		}
		String courseKey = null;
		if ( context_id != null ) {
			courseKey = oauth_consumer_key + ":" + context_id;
		}
		String courseName = context_id;
        if ( request.getParameter("context_title") != null ) 
        	courseName = request.getParameter("context_title");
        if ( request.getParameter("context_label") != null )
            courseName = request.getParameter("context_label");*/
	}
	
	public void doError(HttpServletRequest request, HttpServletResponse response, int errorkey) throws IOException {
		//String return_url = request.getParameter("launch_presentation_return_url"); // Recogemos la url de donde veniamos
	       	/*if ( return_url != null && return_url.length() > 1 ) {
	       		if ( return_url.indexOf('?') > 1 ) {
	               	return_url += "&lti_msg=" + URLEncoder.encode(s);
	            } else {
	          		return_url += "?lti_msg=" + URLEncoder.encode(s);
	            }
	            response.sendRedirect(return_url);
	            return;
	       	}*/
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
		//response.sendRedirect(return_url); // No permitimos entrar y regresamos
		//return;
	}
	
	private IJavaProject createProject(String projectName, String fileCode, String fileName) throws Exception {
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
	    	IPackageFragment fragment = srcFolder.createPackageFragment("src", true, progressMonitor);
	     
	    	StringBuffer buffer = new StringBuffer();
	    	buffer.append("package "+fragment.getElementName()+";\n");
	    	buffer.append(fileCode);
	    	
	    	fragment.createCompilationUnit(fileName, buffer.toString(), false, progressMonitor);
	    } // TODO lanzamos algun tipo de pregunta para sobreescribir o no?
	    
	    return javaProject;
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
