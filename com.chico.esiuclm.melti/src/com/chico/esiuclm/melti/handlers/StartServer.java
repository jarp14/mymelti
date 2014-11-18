package com.chico.esiuclm.melti.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.chico.esiuclm.melti.net.Proxy;

public class StartServer extends AbstractHandler {

	/**
	 * the command has been executed, so extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		if (Proxy.get().getJettyServer().getState() != "STARTED") {
			try {
				Proxy.get().getJettyServer().start();
				MessageDialog.openInformation(window.getShell(), "Melti",
													"Escucha iniciada con éxito");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			MessageDialog.openInformation(window.getShell(), "Melti",
													"La escucha ya se encuentra iniciada");
		}
		
		return null;
	}
	
}
