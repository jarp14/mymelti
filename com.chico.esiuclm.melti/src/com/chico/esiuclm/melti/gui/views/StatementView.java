package com.chico.esiuclm.melti.gui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.chico.esiuclm.melti.gui.Controller;

public class StatementView extends ViewPart {

	public static final String ID = "com.chico.esiuclm.melti.views.statementView";
	private TextViewer viewer;
	// TODO permitir copiar el enunciado
	
	public StatementView() {
		super();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		Document the_statement;
		viewer = new TextViewer(parent, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		viewer.setEditable(false);
		viewer.setInput(Controller.get().getActiveTask());
		the_statement = new Document(Controller.get().getActiveTask().getStatement());
		viewer.setDocument(the_statement);
		
		PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				final String property = evt.getPropertyName();
		    	Display.getDefault().asyncExec(new Runnable() {
		    		public void run() {
		    			if ("statement".equals(property)) {
		    				viewer.getDocument().set(evt.getNewValue().toString());
		    			}
		    		}
		    	});
			}
		};
		
		// Se agrega un listener a la instancia de la tarea del controlador
		Controller.get().getActiveTask().addPropertyChangeListener(propertyChangeListener);
	}
	
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
    }
	
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
}
