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
		int qualified = 0;
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
			qualified = 1;
		}
		
		if(qdialog.open()==1) // Canceló la operación
			return null; 
		
		if (qdialog.getCode().equals("") || qdialog.getFilePath()==null || qdialog.getGrade().isEmpty()) {
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Error");
			dialog.setMessage("Se produjo un error en la captura de parámetros");
			dialog.open();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(255,0,0)));
					my_console.println("["+new Date().toString()+"] ERROR: Se produjo un error en la captura de parámetros");
				}
			});
			return null;
		}
		
		code = qdialog.getCode();
		solution_path = qdialog.getFilePath();
		grade = Double.parseDouble(qdialog.getGrade());
		comments = qdialog.getComments();
		
		if(grade<0 || grade>10) {
			dialog = new MessageBox(window.getShell(), SWT.ICON_ERROR);
			dialog.setText("Error");
			dialog.setMessage("La nota debe estar entre 0 y 10");
			dialog.open();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(255,0,0)));
					my_console.println("["+new Date().toString()+"] ERROR: La nota debe estar entre 0 y 10");
				}
			});
			return null;
		}
		
		// Se le pide confirmar la subida de la tarea seleccionada
		boolean result = MessageDialog.openConfirm(window.getShell(), "Confirmación de envío",
			"Corrección a enviar: "+solution_path+"\n\n"
			+"¿Estás seguro de enviar esta calificación?");
		if (result) { // OK
			// Procede a enviar la calificacion a la BBDD
			try {
				Proxy.get().addQualificationToDB(aux.getStudentID(), aux.getTaskID(), aux.getCourseID(), code, grade, comments);
			} catch (StudentNotLoggedException | TeacherNotLoggedException e) {e.printStackTrace();}
			dialog = new MessageBox(window.getShell(), SWT.ICON_INFORMATION);
			if (qualified==0) dialog.setMessage("Alumno "+aux.getStudent_name_family()+" calificado con éxito");
			else dialog.setMessage("Calificación del alumno "+aux.getStudent_name_family()+" actualizada con éxito");
			dialog.open();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(0,204,0)));
					my_console.println("["+new Date().toString()+"] OK: Alumno "+aux.getStudent_name_family()+" calificado con éxito");
				}
			});
		}
		
		
		
		return null;
	}

}
