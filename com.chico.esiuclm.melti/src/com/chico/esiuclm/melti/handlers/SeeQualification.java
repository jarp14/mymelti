package com.chico.esiuclm.melti.handlers;

import java.util.Date;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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

public class SeeQualification extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		WrappedSolution aux = Controller.get().getActiveWrappedSolution();
		final MessageConsoleStream my_console = MeltiConsole.getMessageConsoleStream("Console");
		MessageBox dialog;
		QualificationDialog qdialog = new QualificationDialog(window.getShell());
		
		if (aux.getCalification()==-1) { // La tarea aún no ha sido calificada, se informa
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Tarea sin calificar");
			dialog.setMessage("Esta tarea aún no ha sido cailficada por su profesor");
			dialog.open();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(204,204,0)));
					my_console.println("["+new Date().toString()+"] WARNING: Esta tarea aún no ha sido calificada por su profesor");
				}
			});
			return null;
		}
		
		qdialog.open();
		
		return null;
	}

}
