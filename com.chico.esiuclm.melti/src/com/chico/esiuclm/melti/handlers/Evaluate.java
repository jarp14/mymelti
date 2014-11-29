package com.chico.esiuclm.melti.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.chico.esiuclm.melti.gui.Controller;
import com.chico.esiuclm.melti.gui.WrappedSolution;
import com.chico.esiuclm.melti.net.Proxy;

public class Evaluate extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		WrappedSolution aux = Controller.get().getActiveWrappedSolution();
		MessageBox dialog;
		MessageDialog msgDialog;
		
		msgDialog = new MessageDialog(window.getShell(), "Confirmar evaluaci�n", null, 
				"�Est�s seguro de evaluar la soluci�n de "+aux.getStudent_name_family()+"?",
				MessageDialog.QUESTION, new String[] {"S�","No"}, 0);
		
		if (msgDialog.open()==0) {
			try {
				if (Proxy.get().createProject(aux.getTask_title(), aux.getStudent_name_family().replace(" ", ""), aux.getSvdCode(), aux.getTaskClassName(), true)) {
					// Si ya existe el proyecto.. se informa
					dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
					dialog.setText("Sobreescritura de proyecto");
					dialog.setMessage("El proyecto "+aux.getTask_title()+"_"+aux.getStudent_name_family().replace(" ", "")+" ya existe."
							+ "\nDebe borrarlo o modificarlo antes de continuar.");
					dialog.open();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

}
