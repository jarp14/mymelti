package com.chico.esiuclm.melti.handlers;

import java.sql.SQLException;
import java.util.Date;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;

import com.chico.esiuclm.melti.exceptions.SolvedErrorException;
import com.chico.esiuclm.melti.exceptions.StudentNotLoggedException;
import com.chico.esiuclm.melti.exceptions.TeacherNotLoggedException;
import com.chico.esiuclm.melti.exceptions.UserNotLoggedException;
import com.chico.esiuclm.melti.gui.console.MeltiConsole;
import com.chico.esiuclm.melti.gui.dialogs.UploadDialog;
import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.net.Proxy;

public class UploadSolution extends AbstractHandler {

	private String task_path, code;
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		final MessageConsoleStream my_console = MeltiConsole.getMessageConsoleStream("Console");
		MessageBox dialog;
		String[] task_context = null;
		UploadDialog udialog = new UploadDialog(window.getShell());
		
		// Comprueba que el alumno esta logeado y de paso recoge el contexto de la tarea
		try {
			task_context = Proxy.get().getContext();
		} catch (UserNotLoggedException | TeacherNotLoggedException | StudentNotLoggedException e1) {
			e1.printStackTrace();
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Error");
			dialog.setMessage("Debes identificarte previamente en Moodle para realizar esta acción");
			dialog.open();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(255,0,0)));
					my_console.println("["+new Date().toString()+"] ERROR: Debes identificarte previamente en Moodle para realizar esta acción");
				}
			});
			return null;
		}
		
		// Comprueba si la tarea ya fue resuelta por este alumno y se encuentra en la BBDD
		try {
			Proxy.get().checkIfSolutionSolved(task_context);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}  catch (SolvedErrorException e1) {
			e1.printStackTrace();
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Error");
			dialog.setMessage("No tienes más intentos para esta tarea");
			dialog.open();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(255,0,0)));
					my_console.println("["+new Date().toString()+"] ERROR: La tarea ya fue resuelta y enviada\nNo tienes más intentos");
				}
			});
			return null;
		}
		
		
		if (udialog.open()==1) //Cancela la subida
			return null; 
		
		task_path = udialog.getFilePath();
		code = udialog.getCode();
	
		if(task_path == null || task_path.equals("")) {
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Error");
			dialog.setMessage("No seleccionó correctamente el fichero a subir");
			dialog.open();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(255,0,0)));
					my_console.println("["+new Date().toString()+"] ERROR: No seleccionó correctamente el fichero a subir");
				}
			});
			return null;
		}
		
		// Pide autorizar la subida de la tarea seleccionada al usuario
		boolean result = MessageDialog.openConfirm(window.getShell(), "Confirmación de envío",
			"Alumno: "+MeltiServer.get().getActiveStudent().getFirst_name()+":"+MeltiServer.get().getActiveStudent().getEmail()+
			"\nTarea a enviar: "+task_path+"\n\n"
			+"¿Estás seguro de enviar esta tarea? No podrás modificar esta acción.");
		if (result) { // OK
			try {
				Proxy.get().uploadSolutionToDB(task_context[0], task_context[1], task_context[2], code);
			} catch (StudentNotLoggedException | TeacherNotLoggedException e) {e.printStackTrace();}
			dialog = new MessageBox(window.getShell(), SWT.ICON_INFORMATION);
			dialog.setMessage("Tarea subida con éxito");
			dialog.open();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(0,204,0)));
					my_console.println("["+new Date().toString()+"] OK: Tarea subida con éxito");
				}
			});
		}
				
		return null;
	}
	
}