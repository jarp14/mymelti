package com.chico.esiuclm.melti.gui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.chico.esiuclm.melti.gui.Controller;
import com.chico.esiuclm.melti.gui.views.WrappedSolution;

public class QualificationDialog extends Dialog {
	
	private Label label;
	private WrappedSolution aux = Controller.get().getActiveWrappedSolution();
	
	public QualificationDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 20;
		layout.marginWidth = 25;
		layout.verticalSpacing = 10;
		layout.numColumns = 2;
		container.setLayout(layout);
		
		label = new Label(container, SWT.LEFT);
		label.setText("Tarea: ");
		label = new Label(container, SWT.BORDER);
		label.setText(aux.getTask_title());
		
		label = new Label(container, SWT.LEFT);
		label.setText("Curso:");
		label = new Label(container, SWT.BORDER);
		label.setText(aux.getCourse_title());
		
		label = new Label(container, SWT.LEFT);
		label.setText("Nota:");
		label = new Label(container, SWT.BORDER);
		label.setText(aux.getCalification()+"");
		
		label = new Label(container, SWT.TOP);
		label.setText("Comentarios: ");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		label = new Label(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		label.setLayoutData(new GridData(GridData.FILL_BOTH));		
		
		return container;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Consultar calificación");
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(640, 480);
	}
	
	@Override
	protected void okPressed() {
		this.close();
	}

}
