package com.chico.esiuclm.melti.gui.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.chico.esiuclm.melti.model.MeltiServer;
import com.chico.esiuclm.melti.model.Student;

public class ProfesorView extends ViewPart {

	private TableViewer viewer;
	
	class ViewContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Object[]) {
				return (Object[]) inputElement;
			}
			return new Object[0];
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object obj, int index) {
			//return getImage(obj);
			return null;
		}

		@Override
		public String getColumnText(Object obj, int columnIndex) {
			Student student = (Student) obj;
			switch (columnIndex) {
				case 0:
					return student.getID()+"";
				case 1:
					return student.getFirst_name();
				case 2:
					return student.getLast_name();
				case 3:
					return "?";
				default:
					return "desconocido "+columnIndex;
			}
			//return getText(obj);
		}
		
		/*public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}*/
	}
	
	
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		String[] columnNames = new String[] {
				"ID", "Nombre", "Apellidos", "E-mail", "Calificación" };
		int[] columnWidths = new int[] { 50, 100, 100, 100, 100 };
		int[] columnAlignments = new int[] { SWT.LEFT , SWT.LEFT , SWT.LEFT , SWT.LEFT , SWT.LEFT };
		
		for (int i=0; i<columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(table, columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}
		
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setContentProvider(new ViewContentProvider());		
		viewer.setInput(MeltiServer.get().getStudent("0"));
		//viewer.setInput(MeltiServer.get().getUser());
		
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	/*public void refresh() { 
		viewer.getTable().getDisplay().asyncExec(new Runnable() { 
			public void run() { 
				viewer.refresh(); 
			} 
		}); 
	}*/ 

}