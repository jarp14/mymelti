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
import com.chico.esiuclm.melti.gui.Controller;
import com.chico.esiuclm.melti.gui.console.MeltiConsole;
import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.net.Proxy;

public class RefreshTasksView extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final MessageConsoleStream my_console = MeltiConsole.getMessageConsoleStream("Console");
		
		try {
			// Solo refresca si la informacion esta preparada
			if(Proxy.get().contextPrepared(true)) {
				String user_id = MeltiServer.get().getActiveStudent().getID();
			
				Controller.get().updateStudentTasksView(user_id);
			}
		} catch (StudentNotLoggedException | TeacherNotLoggedException e) {
			e.printStackTrace();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(204,204,0)));
					my_console.println("["+new Date().toString()+"] WARNING: El alumno debe identificarse en Moodle antes de realizar esta acción");
				}
			});
		}
		
		return null;
	}
	
}
