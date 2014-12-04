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
import com.chico.esiuclm.melti.gui.views.WrappedSolution;
import com.chico.esiuclm.melti.net.Proxy;

public class SeeSolution extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		WrappedSolution aux = Controller.get().getActiveWrappedSolution();
		final MessageConsoleStream my_console = MeltiConsole.getMessageConsoleStream("Console");
		MessageBox dialog;
		MessageDialog msgDialog;
		
		msgDialog = new MessageDialog(window.getShell(), "Confirmar evaluación", null, 
				"¿Estás seguro de querer evaluar la solución de "+aux.getStudent_name_family()+"?",
				MessageDialog.QUESTION, new String[] {"Sí","No"}, 0);
		
		if(msgDialog.open()==1) 
			return null;
			
		try {
			if (Proxy.get().createProject(aux.getTask_title(), aux.getStudent_name_family().replace(" ",""), 
					aux.getSvdCode(), aux.getTaskClassName(), true)) {
				// Si ya existe el proyecto.. se informa
				dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
				dialog.setText("Sobreescritura de proyecto");
				dialog.setMessage("El proyecto "+aux.getTask_title()+"_"+aux.getStudent_name_family().replace(" ", "")+" ya existe."
						+ "\nDebe borrarlo o modificarlo antes de continuar.");
				dialog.open();
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						my_console.setColor(new Color(null, new RGB(255,0,0)));
						my_console.println("["+new Date().toString()+"] ERROR: El proyecto ya existe\nDebe borrarlo o modificarlo antes de continuar");
					}
				});
			}
		} catch (Exception e) {
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(255,0,0)));
					my_console.println("["+new Date().toString()+"] ERROR: Se produjo un error inesperado al generar el proyecto");
				}
			});
		}
		
		return null;
	}

}
