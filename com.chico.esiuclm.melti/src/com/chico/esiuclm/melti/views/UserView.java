package com.chico.esiuclm.melti.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.chico.esiuclm.melti.model.MeltiServer;

public class UserView extends ViewPart {

	private TableViewer viewer;
	
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		String[] columnNames = new String[] {
				"ID", "Nombre", "Apellidos", "E-mail", "Rol" };
		int[] columnWidths = new int[] { 50, 100, 100, 100, 100 };
		int[] columnAlignments = new int[] { SWT.LEFT , SWT.LEFT , SWT.LEFT , SWT.LEFT , SWT.LEFT };
		
		for (int i=0; i<columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(table, columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}
		
		viewer.setLabelProvider(new UserLabelProvider());
		viewer.setContentProvider(new ArrayContentProvider());		
		//viewer.setInput(MeltiServer.get().getUser());
		
	}

	@Override
	public void setFocus() {}
	
	public void refresh() { 
		viewer.getTable().getDisplay().asyncExec(new Runnable() { 
			public void run() { 
				viewer.refresh(); 
			} 
		}); 
	} 

}