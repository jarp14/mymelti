package com.chico.esiuclm.melti.handlers;

import java.util.Date;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;

import com.chico.esiuclm.melti.gui.console.MeltiConsole;
import com.chico.esiuclm.melti.net.Proxy;

public class StartServer extends AbstractHandler {

	/**
	 * the command has been executed, so extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		final MessageConsoleStream my_console = MeltiConsole.getMessageConsoleStream("Console");
		
		if (Proxy.get().getJettyServer().getState() != "STARTED") {
			try {
				Proxy.get().getJettyServer().start();
				MessageDialog.openInformation(window.getShell(), "Jetty Server", "Escucha iniciada con éxito");
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						my_console.setColor(new Color(null, new RGB(0,204,0)));
						my_console.println("["+new Date().toString()+"] OK: La escucha del servidor Jetty ha sido iniciada con éxito");
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			MessageDialog.openInformation(window.getShell(), "Jetty Server", "La escucha ya se encuentra iniciada");
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					my_console.setColor(new Color(null, new RGB(204,204,0)));
					my_console.println("["+new Date().toString()+"] WARNING: La escucha del servidor Jetty ya se encuentra iniciada");
				}
			});
		}
		
		return null;
	}
	
}
