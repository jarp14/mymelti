package com.chico.esiuclm.melti.handlers;

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

import com.chico.esiuclm.melti.gui.Controller;
import com.chico.esiuclm.melti.gui.console.MeltiConsole;
import com.chico.esiuclm.melti.gui.dialogs.QualificationDialog;
import com.chico.esiuclm.melti.gui.views.WrappedSolution;
import com.chico.esiuclm.melti.net.Proxy;

public class SeeQualification extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		WrappedSolution aux = Controller.get().getActiveWrappedSolution();
		final MessageConsoleStream my_console = MeltiConsole.getMessageConsoleStream("Console");
		MessageBox dialog;
		MessageDialog msgDialog;
		QualificationDialog qdialog = new QualificationDialog(window.getShell());
		
		if (aux.getCalification()==-1) { // La tarea aún no ha sido calificada, se informa
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Tarea sin calificar");
			dialog.setMessage("Esta tarea aún no ha sido calificada por su profesor");
			dialog.open();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(204,204,0)));
					my_console.println("["+new Date().toString()+"] WARNING: Esta tarea aún no ha sido calificada por su profesor");
				}
			});
			return null;
		}
		
		msgDialog = new MessageDialog(window.getShell(), "Confirmar", null, 
				"¿Estás seguro de querer revisar las anotaciones del profesor en la tarea "+aux.getTask_title()+"?",
				MessageDialog.QUESTION, new String[] {"Sí","No"}, 0);
		
		if (msgDialog.open()==1) // No quiere ver la calificación
			return null;
		
		try {
			if (Proxy.get().createProject(aux.getTask_title(), aux.getStudent_name_family().replace(" ",""), 
					aux.getSvdCode(), aux.getTaskClassName(), true)) {
				// Si ya existe el proyecto.. se informa
				dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
				dialog.setText("Sobreescritura de proyecto");
				dialog.setMessage("El proyecto "+aux.getTask_title().replace(" ", "")+"_"+aux.getStudent_name_family().replace(" ", "")+" ya existe"
						+ "\nDebe borrarlo o modificarlo antes de continuar");
				dialog.open();
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						my_console.setColor(new Color(null, new RGB(255,0,0)));
						my_console.println("["+new Date().toString()+"] ERROR: El proyecto ya existe\nDebe borrarlo o modificarlo antes de continuar");
					}
				});
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(255,0,0)));
					my_console.println("["+new Date().toString()+"] ERROR: Se produjo un error inesperado al generar el proyecto");
				}
			});
		}
		
		qdialog.open();
		
		
		
		return null;
	}

}
