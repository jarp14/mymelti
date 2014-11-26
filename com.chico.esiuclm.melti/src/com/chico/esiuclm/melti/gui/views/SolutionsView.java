package com.chico.esiuclm.melti.gui.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.chico.esiuclm.melti.gui.Controller;
import com.chico.esiuclm.melti.model.Student;

public class SolutionsView extends ViewPart {

	private TableViewer viewer;
	
	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);       
		
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(Controller.get().getStudentsWithSolution());
		
		getSite().setSelectionProvider(viewer);
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = {"ID", "Nombre", "Apellidos", "Email", "ID Curso" };
		int[] bounds = { 100, 100, 100, 100, 100 };
		
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				Student s = (Student) element;
				return s.getID();
			}
		});
		
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				Student s = (Student) element;
				return s.getFirst_name();
			}
		});
		
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				Student s = (Student) element;
				return s.getLast_name();
			}
		});
		
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				Student s = (Student) element;
				return s.getEmail();
			}
		});
		
		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				Student s = (Student) element;
				return s.getCourseID();
			}
			
			/*public Image getImage(Object element) {
				//if ((Student)element).isOnline()) {
				// return CHECKED;
				return UNCHECKED;
			}*/
		});
	}
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}
	
	public TableViewer getViewer() {
		return viewer;
	}

}
