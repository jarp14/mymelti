package com.chico.esiuclm.melti.handlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.chico.esiuclm.melti.exceptions.UserNotLoggedException;
import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.net.Proxy;

public class UploadSolution extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageBox dialog;
		FileDialog fdialog;
		BufferedReader br = null;
		String code = "";
		String[] ids = null;
		
		try { // Comprueba que el estudiante esta logeado y de paso recoge los ids
			ids = Proxy.get().getIDs();
		} catch (UserNotLoggedException e1) {
			e1.printStackTrace();
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Error");
			dialog.setMessage("Debes identificarte previamente en Moodle para poder subir una tarea");
			dialog.open();
			return null;
		}
		
		// Muestra un dialogo para subir la tarea
		fdialog = new FileDialog(window.getShell(), SWT.OPEN);
		fdialog.setFilterExtensions(new String [] {"*.java"});
		fdialog.setFilterPath("c:\\");
		String file_path = fdialog.open();
		if (file_path != null) { // El alumno abrio un fichero para subirlo, recoge el codigo
			try {
				String currentLine;
				br = new BufferedReader(new FileReader(file_path));
				while ((currentLine = br.readLine()) != null) {
					code += currentLine+"\n";
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			// Pide autorizar la subida de la tarea seleccionada al usuario
			boolean result = MessageDialog.openConfirm(window.getShell(), "Confirmación de envío", 
					"Alumno: "+MeltiServer.get().getActiveStudent().getFirst_name()+":"+MeltiServer.get().getActiveStudent().getEmail()+
					"\n\nTarea a enviar: "+file_path+"\n\n"
					+"¿Estás seguro de enviar esta tarea? No podrás modificar esta acción.");
			if (result) { // OK
				Proxy.get().addSolutionToDB(ids[0], ids[1], ids[2], code);
				dialog = new MessageBox(window.getShell(), SWT.ICON_INFORMATION);
				dialog.setMessage("Tarea subida con éxito");
				dialog.open();
			}
		}
		
		return null;
	}
	
}