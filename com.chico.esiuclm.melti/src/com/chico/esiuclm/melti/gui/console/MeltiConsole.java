package com.chico.esiuclm.melti.gui.console;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class MeltiConsole {
	
	public static MessageConsole findConsole(String name)  {
        ConsolePlugin plugin = ConsolePlugin.getDefault();
        IConsoleManager conMan = plugin.getConsoleManager();
        IConsole[] existing = conMan.getConsoles();
        for (int i=0; i<existing.length; i++)
            if (name.equals(existing[i].getName())) 
            	return (MessageConsole) existing[i];
        // Si no la encuentra creamos una
        MessageConsole myConsole = new MessageConsole(name, null);
        conMan.addConsoles(new IConsole[]{myConsole});
        return myConsole;
    }

    public static MessageConsoleStream getMessageConsoleStream(String name) {
        MessageConsole myConsole = findConsole(name);
        MessageConsoleStream out = myConsole.newMessageStream();
        return out;
    }
    
}
