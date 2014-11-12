package com.chico.esiuclm.melti.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.chico.esiuclm.melti.model.User;

public class UserLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		User user = (User) element;
		switch(columnIndex) {
			case 0:
				//return user.getID();
			case 1:
				return user.getFirst_name();
			case 2:
				return user.getLast_name();
			case 3:
				return user.getEmail();
			case 4:
				return user.getRole();
			default:
				return "desconocido " + columnIndex;
		}
	}
	
}
