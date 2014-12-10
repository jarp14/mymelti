package com.chico.esiuclm.melti.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.chico.esiuclm.melti.gui.Controller;
import com.chico.esiuclm.melti.model.MeltiServer;

public class RefreshTasksView extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String user_id = MeltiServer.get().getActiveStudent().getID();
		
		Controller.get().updateStudentTasksView(user_id);
		
		return null;
	}
	
}
