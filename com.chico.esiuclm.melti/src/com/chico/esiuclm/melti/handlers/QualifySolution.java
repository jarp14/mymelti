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

import com.chico.esiuclm.melti.exceptions.StudentNotLoggedException;
import com.chico.esiuclm.melti.exceptions.TeacherNotLoggedException;
import com.chico.esiuclm.melti.gui.Controller;
import com.chico.esiuclm.melti.gui.console.MeltiConsole;
import com.chico.esiuclm.melti.gui.dialogs.QualifyDialog;
import com.chico.esiuclm.melti.gui.views.WrappedSolution;
import com.chico.esiuclm.melti.net.Proxy;

/*
 * Comando activado por el profesor, utilizado para calificar 
 * la solución de un alumno
 */
public class QualifySolution extends AbstractHandler {
	
	private double grade;
	private String solution_path, code, comments;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		final WrappedSolution aux = Controller.get().getActiveWrappedSolution(); // La solucion seleccionada, a evaluar
		final MessageConsoleStream my_console = MeltiConsole.getMessageConsoleStream("Console");
		MessageBox dialog;
		QualifyDialog qdialog = new QualifyDialog(window.getShell());
		
		if (aux.getCalification()!=-1) { // La tarea ya fue calificada, se informa y se pregunta
			boolean result = MessageDialog.openConfirm(window.getShell(), "Solución evaluada",
					"Esta tarea ya fue calificada, ¿deseas volver a evaluarla?");
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(204,204,0)));
					my_console.println("["+new Date().toString()+"] WARNING: Esta tarea ya fue calificada");
				}
			});
			if (!result) return null;
		}
		
		if(qdialog.open()==1) // Canceló la operación
			return null; 
		
		code = qdialog.getCode();
		solution_path = qdialog.getFilePath();
		grade = Double.parseDouble(qdialog.getGrade());
		comments = qdialog.getComments();
		
		if(grade<0 || grade>10) {
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Error");
			dialog.setMessage("La calificación debe estar entre 0 y 10");
			dialog.open();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(255,0,0)));
					my_console.println("["+new Date().toString()+"] ERROR: La calificación debe estar entre 0 y 10");
				}
			});
			return null;
		}
		
		if(solution_path == null || solution_path.equals("")) {
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Error");
			dialog.setMessage("No seleccionó correctamente el fichero");
			dialog.open();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(255,0,0)));
					my_console.println("["+new Date().toString()+"] ERROR: No seleccionó correctamente el fichero");
				}
			});
			return null;
		}
		
		// Procede a enviar la calificacion a la BBDD
		try {
			Proxy.get().addQualificationToDB(aux.getStudentID(), aux.getTaskID(), aux.getCourseID(), code, grade, comments);
		} catch (StudentNotLoggedException | TeacherNotLoggedException e) {
			e.printStackTrace();
		}
		dialog = new MessageBox(window.getShell(), SWT.ICON_INFORMATION);
		dialog.setMessage("Alumno "+aux.getStudent_name_family()+" calificado con éxito");
		dialog.open();
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				my_console.setColor(new Color(null, new RGB(0,204,0)));
				my_console.println("["+new Date().toString()+"] OK: Alumno "+aux.getStudent_name_family()+" calificado con éxito");
			}
		});
		
		return null;
	}

}
