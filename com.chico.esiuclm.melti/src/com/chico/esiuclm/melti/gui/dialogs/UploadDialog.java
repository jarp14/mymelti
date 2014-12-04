package com.chico.esiuclm.melti.gui.dialogs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.chico.esiuclm.melti.model.MeltiServer;

public class UploadDialog extends Dialog {

	private Label label;
	private Text input_file;
	private Button browse_button;
	private FileDialog fdialog;
	
	private String code, file_path;
	
	public UploadDialog(Shell parentShell) {
		super(parentShell);
		fdialog = new FileDialog(parentShell, SWT.OPEN);
		fdialog.setFilterExtensions(new String [] {"*.java"});
		fdialog.setFilterPath("C:\\");
		code = "";
	}
	
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 20;
		layout.marginWidth = 25;
		layout.verticalSpacing = 10;
		layout.numColumns = 3;
		container.setLayout(layout);
		
		label = new Label(container, SWT.LEFT);
		label.setText("Alumno: ");
		label = new Label(container, SWT.BORDER);
		label.setText(MeltiServer.get().getActiveStudent().getFirst_name()+
				" ("+MeltiServer.get().getActiveStudent().getEmail()+") ");
		new Label(container, SWT.NONE);
		
		label = new Label(container, SWT.LEFT);
		label.setText("Tarea: ");
		label = new Label(container, SWT.BORDER);
		label.setText(MeltiServer.get().getActiveTask().getTitle());
		new Label(container, SWT.NONE);
		
		label = new Label(container, SWT.LEFT);
		label.setText("Curso: ");
		label = new Label(container, SWT.BORDER);
		label.setText(MeltiServer.get().getActiveCourse().getTitle());
		new Label(container, SWT.NONE);
		
		label = new Label(container, SWT.LEFT);
		label.setText("Selecciona el fichero: ");		
		input_file = new Text(container, SWT.SINGLE | SWT.BORDER);
		input_file.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		browse_button = new Button(container, SWT.PUSH);
		browse_button.setText("Buscar...");
		browse_button.setToolTipText("Selecciona el fichero comentado");
		browse_button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				file_path = fdialog.open();
				BufferedReader br = null;
				if (file_path != null) { // El alumno abrio un fichero para subirlo, recoge el codigo
					try {
						input_file.setText(file_path);
						String currentLine;
						br = new BufferedReader(new FileReader(file_path));
						while ((currentLine = br.readLine()) != null) {
							code += currentLine+"\n";
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					} finally {
						try {
							if (br != null)
								br.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
			
		return container;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Enviar solución");
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(460, 210);
	}
	
	@Override
	protected void okPressed() {
		this.close();
	}
	
	public String getCode() {
		return code;
	}
	
	public String getFilePath() {
		if(file_path != null) return file_path;
		else return null;
	}

}
