package com.chico.esiuclm.melti.handlers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.apache.commons.codec.binary.Base64;

public class RestClient extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		//MeltiFileDialog dialog = new MeltiFileDialog(Display.getCurrent());
		//String filePath = dialog.getFileLocation();
        String token = "939fd7e6723c4622cfc2a169536227dc";
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
        
        	String functionName = "core_files_upload";
        	String urlParameters;
        	urlParameters = "&contextid="+URLEncoder.encode("12", "UTF-8") +
        			"&component="+URLEncoder.encode("user", "UTF-8") +
        			"&filearea="+URLEncoder.encode("private", "UTF-8") +
        			"&filepath="+URLEncoder.encode("/", "UTF-8") +
        			"&itemid="+URLEncoder.encode("0", "UTF-8") +        			
        			"&filename="+URLEncoder.encode("Persona.java", "UTF-8") +
        			"&filecontent="+URLEncoder.encode(encodeFileToBase64Binary("D:\\PFC\\workspace\\com.chico.esiuclm.melti\\src\\com\\chico\\esiuclm\\melti\\model\\Persona.java"),"UTF-8");
        			 //+"&url="+URLEncoder.encode("D:\\PFC\\workspace\\com.chico.esiuclm.melti\\src\\com\\chico\\esiuclm\\melti\\model\\user.java","UTF-8");
        	
        /*String functionName = "core_user_create_users";
        String urlParameters = "users[0][username]=" + URLEncoder.encode("testusername1", "UTF-8") +
			"&users[0][password]=" + URLEncoder.encode("Testpassword_1", "UTF-8") +
			"&users[0][firstname]=" + URLEncoder.encode("testfirstname1", "UTF-8") +
			"&users[0][lastname]=" + URLEncoder.encode("testlastname1", "UTF-8") +
			"&users[0][email]=" + URLEncoder.encode("testemail1@moodle.com", "UTF-8") +
			"&users[0][auth]=" + URLEncoder.encode("manual", "UTF-8") +
			"&users[0][idnumber]=" + URLEncoder.encode("testidnumber1", "UTF-8") +
			"&users[0][lang]=" + URLEncoder.encode("en", "UTF-8") +
			"&users[0][theme]=" + URLEncoder.encode("standard", "UTF-8") +
			"&users[0][timezone]=" + URLEncoder.encode("-12.5", "UTF-8") +
			"&users[0][mailformat]=" + URLEncoder.encode("0", "UTF-8") +
			"&users[0][description]=" + URLEncoder.encode("Hello World!", "UTF-8") +
			"&users[0][city]=" + URLEncoder.encode("testcity1", "UTF-8") +
			"&users[0][country]=" + URLEncoder.encode("au", "UTF-8") +
			"&users[0][preferences][0][type]=" + URLEncoder.encode("preference1", "UTF-8") +
			"&users[0][preferences][0][value]=" + URLEncoder.encode("preferencevalue1", "UTF-8") +
			"&users[0][preferences][1][type]=" + URLEncoder.encode("preference2", "UTF-8") +
			"&users[0][preferences][1][value]=" + URLEncoder.encode("preferencevalue2", "UTF-8") +
			"&users[1][username]=" + URLEncoder.encode("testusername2", "UTF-8") +
			"&users[1][password]=" + URLEncoder.encode("Testpassword_2", "UTF-8") +
			"&users[1][firstname]=" + URLEncoder.encode("testfirstname2", "UTF-8") +
			"&users[1][lastname]=" + URLEncoder.encode("testlastname2", "UTF-8") +
			"&users[1][email]=" + URLEncoder.encode("testemail2@moodle.com", "UTF-8") +
			"&users[1][timezone]=" + URLEncoder.encode("Pacific/Port_Moresby", "UTF-8");*/
		

        // Llamada REST
        // Send request
        String serverurl = domainName + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=" + functionName;
        //String serverurl = domainName + "/webservice/upload.php" + "?wstoken=" + token + "&wsfunction=" + functionName;
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
	
	// Codifica a 64 bits
	private String encodeFileToBase64Binary(String fileName) throws IOException {
 
		File file = new File(fileName);
		byte[] bytes = loadFile(file);
		//byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = Base64.encodeBase64String(bytes);
 
		return encodedString;
	}
	
	// Carga el fichero
	@SuppressWarnings("resource")
	private static byte[] loadFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);
 
	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }
 
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }
 
	    is.close();
	    return bytes;
	}
	
}
