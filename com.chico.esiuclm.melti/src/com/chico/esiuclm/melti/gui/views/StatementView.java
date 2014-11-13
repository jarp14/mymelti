package com.chico.esiuclm.melti.gui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.chico.esiuclm.melti.model.MeltiServer;

public class StatementView extends ViewPart {

	public static final String ID = "com.chico.esiuclm.melti.views.statementView";
	private TextViewer viewer;
	
	public StatementView() {
		super();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		final Document statement;
		viewer = new TextViewer(parent, SWT.MULTI | SWT.V_SCROLL);
		viewer.setEditable(false);
		statement = new Document("Enunciado del problema a resolver por el alumno");
		viewer.setDocument(statement);
		
		PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
		      public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		        String property = propertyChangeEvent.getPropertyName();
		        if ("statement".equals(property)) {
		        	statement.set(propertyChangeEvent.getNewValue().toString());
		        }
		      }
		};
		
		//MeltiServer.get().getTask().addPropertyChangeListener(propertyChangeListener);
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
