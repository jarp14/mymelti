package com.chico.esiuclm.melti.handlers;

import java.util.Date;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsoleStream;

import com.chico.esiuclm.melti.exceptions.StudentNotLoggedException;
import com.chico.esiuclm.melti.exceptions.TeacherNotLoggedException;
import com.chico.esiuclm.melti.gui.console.MeltiConsole;
import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.net.Proxy;

public class RefreshSolutionsView extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final MessageConsoleStream my_console = MeltiConsole.getMessageConsoleStream("Console");
		
		try {
			// Solo refresca si hay alguien identificado, el contexto esta preparado
			if(Proxy.get().contextPrepared(false)) {
				String course_id = MeltiServer.get().getActiveCourse().getID();
				String task_id = MeltiServer.get().getActiveTask().getID();
			
				Proxy.get().updateSolutionsView(task_id, course_id);
			}
		} catch (StudentNotLoggedException | TeacherNotLoggedException e) {
			e.printStackTrace();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(204,204,0)));
					my_console.println("["+new Date().toString()+"] WARNING: El profesor debe identificarse en Moodle antes de realizar esta acción");
				}
			});
		}
		
		return null;
	}
	
}