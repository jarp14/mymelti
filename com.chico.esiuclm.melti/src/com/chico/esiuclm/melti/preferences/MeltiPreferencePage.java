package com.chico.esiuclm.melti.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.chico.esiuclm.melti.MeltiPlugin;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class MeltiPreferencePage extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {
	
	private RadioGroupFieldEditor rolePrefEditor;
	private StringFieldEditor idPrefEditor, tokenPrefEditor, 
									keyPrefEditor, secretPrefEditor;	
	
	public MeltiPreferencePage() {
		super(GRID);
		setPreferenceStore(MeltiPlugin.getDefault().getPreferenceStore());
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		rolePrefEditor = new RadioGroupFieldEditor(
				IPreferenceConstants.MELTI_ROLE, 
				"Rol", 
				1,
				new String[][] { 
						{ "Alumno", IPreferenceConstants.MELTI_ROLE_STUDENT }, 
						{ "Profesor", IPreferenceConstants.MELTI_ROLE_PROFESOR } }, 
				getFieldEditorParent());
		idPrefEditor = new StringFieldEditor(IPreferenceConstants.MELTI_USERID, "ID Usuario", getFieldEditorParent());
		keyPrefEditor = new StringFieldEditor(IPreferenceConstants.MELTI_KEY, "Key", getFieldEditorParent());
		secretPrefEditor = new StringFieldEditor(IPreferenceConstants.MELTI_SECRET, "Secret", getFieldEditorParent());
		tokenPrefEditor = new StringFieldEditor(IPreferenceConstants.MELTI_TOKEN, "Token", getFieldEditorParent());
		addField(rolePrefEditor);
		addField(idPrefEditor);
		addField(keyPrefEditor);
		addField(secretPrefEditor);
		addField(tokenPrefEditor);
	}

	@Override
	public void init(IWorkbench workbench) { }
	
	/** 
	 * Si existen cambios en las preferencias se avisa del evento 
	 * para checkear el estado 
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
	    if (event.getProperty().equals(FieldEditor.VALUE)) {
	    	if (event.getSource() == idPrefEditor)
	    		checkState();
	    }
	}
	
	// Se comprueba el estado de las preferencias
	@Override
	protected void checkState() {
		super.checkState();
		if (idPrefEditor.getStringValue()!=null && !idPrefEditor.getStringValue().equals("")) {
			setErrorMessage(null);
			setValid(true);
		} else {
			setErrorMessage("Error: El campo ID usuario no puede estar vacio");
			setValid(false);
		}
		
	}
	
}