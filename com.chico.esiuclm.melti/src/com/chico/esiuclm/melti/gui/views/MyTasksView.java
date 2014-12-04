package com.chico.esiuclm.melti.gui.views;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.IServiceLocator;

import com.chico.esiuclm.melti.gui.Controller;

public class MyTasksView extends ViewPart {

	public static final String ID = "com.chico.esiuclm.melti.views.mytasksView";
	private TableViewer viewer;
	private Action seeQualificationAction;
	// TODO refrescar la vista
	
	@Override
	public void createPartControl(Composite parent) {
		TableColumnLayout layout = new TableColumnLayout();
		parent.setLayout(layout);
		
		viewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableViewerColumn tvc1 = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn cTaskTitle = tvc1.getColumn();
		layout.setColumnData(cTaskTitle, new ColumnWeightData(4, ColumnWeightData.MINIMUM_WIDTH, true));
		cTaskTitle.setText("Tarea");
		cTaskTitle.setResizable(true);
		cTaskTitle.setMoveable(true);

		TableViewerColumn tvc4 = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn cCourse = tvc4.getColumn();
		layout.setColumnData(cCourse, new ColumnWeightData(6, ColumnWeightData.MINIMUM_WIDTH, true));
		cCourse.setText("Curso");
		cCourse.setResizable(true);
		cCourse.setMoveable(true);

		TableViewerColumn tvc5 = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn cGrade = tvc5.getColumn();
		layout.setColumnData(cGrade, new ColumnWeightData(4, ColumnWeightData.MINIMUM_WIDTH, true));
		cGrade.setText("Calificación");
		cGrade.setResizable(true);
		cGrade.setMoveable(true);
		
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new TableLabelProvider());
		
		viewer.setInput(Controller.get().getWrappedSolutions());
		getSite().setSelectionProvider(viewer);
		
		createActions();
		createContextMenu();
		//createToolbar();
		
		hookDoubleClick();
	}
	
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			WrappedSolution ws = (WrappedSolution) element;
			String result = "";
			switch(columnIndex){
			case 0:
				result = ws.getTask_title();
				break;
			case 1:
				result = ws.getCourse_title();
				break;
			case 2:
				if(ws.getCalification()==-1.0) result = "No calificado";
				else result = ws.getCalification()+"";
				break;
			default:
				result = "";
			}
			return result;
		}
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public void refresh() {
		// Llamada al controlador para que actualice el Array TODO
		viewer.refresh();
	}
		
	public void createActions() {
		seeQualificationAction = new Action("Ver calificación") {
			public void run() {
				seeQualification();
			}
		};
	}
	
	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});
		
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		
		getSite().registerContextMenu(menuMgr, viewer);
	}
	
	private void hookDoubleClick() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				seeQualification();
			}	
		});
	}
	
	private void fillContextMenu(IMenuManager mgr) {
		mgr.add(seeQualificationAction);
	}
	
	private void seeQualification() {
		executeCommand("com.chico.esiuclm.melti.commands.seeQualification");
	}
	
	private void executeCommand(String the_command) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		if (selection.isEmpty()) return;
		Controller.get().setActiveWrappedSolution((WrappedSolution) selection.getFirstElement()); // Obtenemos los valores para trabajar con ellos
		IServiceLocator serviceLocator = PlatformUI.getWorkbench();
		ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);
		Command command = commandService.getCommand(the_command);
		try {
			command.executeWithChecks(new ExecutionEvent());
		} catch (ExecutionException | NotDefinedException
				| NotEnabledException | NotHandledException e) {
			e.printStackTrace();
		}
	}
	
	/*private void createMenu() {
		IMenuManager mgr = getViewSite().getActionBars().getMenuManager();
		mgr.add(selectAllAction);
	}*/

	/*private void createToolbar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(selectAllAction);
	}*/

}
