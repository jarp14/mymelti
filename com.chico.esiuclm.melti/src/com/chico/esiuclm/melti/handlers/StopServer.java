package com.chico.esiuclm.melti.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.chico.esiuclm.melti.MeltiPlugin;
import com.chico.esiuclm.melti.model.MeltiServer;

public class StopServer extends AbstractHandler {
	
	/**
	 * the command has been executed, so extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		if(MeltiServer.get().getJettyServer().getState().equals("STARTED")) {
			try {
				MeltiServer.get().getJettyServer().stop();
				MessageDialog.openInformation(window.getShell(), "Melti",
						"Escucha pausada con éxito");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			MessageDialog.openInformation(window.getShell(), "Melti",
					"La escucha ya se encuentra pausada");
		}
		
		return null;
	}
	
}
