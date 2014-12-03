package com.chico.esiuclm.melti.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.chico.esiuclm.melti.gui.Controller;
import com.chico.esiuclm.melti.gui.WrappedSolution;
import com.chico.esiuclm.melti.gui.dialog.QualifyDialog;

public class Qualify extends AbstractHandler {
	
	private double grade;
	private String solution_path, code, comments;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		WrappedSolution aux = Controller.get().getActiveWrappedSolution();
		MessageBox dialog;
		QualifyDialog qdialog = new QualifyDialog(window.getShell());
		qdialog.open();
		
		code = qdialog.getCode();
		solution_path = qdialog.getFilePath();
		grade = Double.parseDouble(qdialog.getGrade());
		comments = qdialog.getComments();
		
		if(grade<0 || grade>10) {
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Error");
			dialog.setMessage("La calificación debe estar entre 0 y 10");
			dialog.open();
			return null;
		}
		
		if(solution_path == null || solution_path.equals("")) {
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Error");
			dialog.setMessage("No seleccionó correctamente el fichero");
			dialog.open();
			return null;
		}		
		
		// Si todo ha ido bien... TODO

		return null;
	}

}
