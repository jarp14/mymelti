package com.chico.esiuclm.melti.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.net.Proxy;

public class RefreshSolutionsView extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String course_id = MeltiServer.get().getActiveCourse().getID();
		String task_id = MeltiServer.get().getActiveTask().getID();
		
		Proxy.get().updateSolutionsView(task_id, course_id);
		
		return null;
	}
	
}