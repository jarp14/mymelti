package com.chico.esiuclm.melti.handlers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class UploadGradeRest extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
        String token = "2c93ee3d07b07cc084b3aba95477d06a";
		String domainName = "http://localhost";
		        
        /// Formato de retorno en la respuesta del servidor
        String restformat = "xml"; // XML o JSON     
        if (restformat.equals("json")) {
            restformat = "&moodlewsrestformat=" + restformat;
        } else {
            restformat = "";
        }
        
        // Parametros a enviar
        try {
        
        	String functionName = "mod_assign_save_grade";
        	String urlParameters;
        	urlParameters = "&assignmentid="+URLEncoder.encode("1", "UTF-8") +
        	"&userid="+URLEncoder.encode("3", "UTF-8") +
        	"&grade="+URLEncoder.encode("8.0", "UTF-8")+
        	"&attemptnumber="+URLEncoder.encode("-1","UTF-8")+
        	"&addattempt="+URLEncoder.encode("0","UTF-8")+
        	"&workflowstate="+URLEncoder.encode("graded","UTF-8")+
        	"&applytoall="+URLEncoder.encode("0","UTF-8")+
        	"&plugindata[assignfeedbackcomments_editor][text]="+URLEncoder.encode("test","UTF-8")+
        	"&plugindata[assignfeedbackcomments_editor][format]="+URLEncoder.encode("1","UTF-8")+
        	"&plugindata[files_filemanager]="+URLEncoder.encode("0","UTF-8");
        // Llamada REST
        // Send request
        String serverurl = domainName + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=" + functionName;
        HttpURLConnection con;
		
			con = (HttpURLConnection) new URL(serverurl).openConnection();
			con.setRequestMethod("POST");
	        con.setRequestProperty("Content-Type",
	           "application/x-www-form-urlencoded");
	        con.setRequestProperty("Content-Language", "en-US");
	        con.setDoOutput(true);
	        con.setUseCaches (false);
	        con.setDoInput(true);
	        DataOutputStream wr = new DataOutputStream (
	                  con.getOutputStream ());
	        wr.writeBytes(urlParameters);
	        wr.flush ();
	        wr.close ();
	        
	      //Get Response
	      InputStream is = con.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuilder response = new StringBuilder();
	      while((line = rd.readLine()) != null) {
	          response.append(line);
	          response.append('\r');
	      }
	      rd.close();
	      MessageDialog.openInformation(window.getShell(), "Melti", response.toString());
	      
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return null;
	}
}
