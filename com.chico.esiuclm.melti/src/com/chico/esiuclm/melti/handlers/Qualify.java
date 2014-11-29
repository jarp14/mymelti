package com.chico.esiuclm.melti.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.chico.esiuclm.melti.gui.Controller;
import com.chico.esiuclm.melti.gui.WrappedSolution;
import com.chico.esiuclm.melti.gui.dialog.QualifyDialog;

public class Qualify extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		WrappedSolution aux = Controller.get().getActiveWrappedSolution();
		QualifyDialog qdialog = new QualifyDialog(window.getShell());
		qdialog.open();
		
		return null;
	}

}
