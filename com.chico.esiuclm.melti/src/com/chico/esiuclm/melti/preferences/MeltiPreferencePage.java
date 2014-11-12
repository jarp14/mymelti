package com.chico.esiuclm.melti.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
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
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		rolePrefEditor = new RadioGroupFieldEditor(PreferenceConstants.MELTI_ROLE, "Rol", 1,
			new String[][] { { "Alumno", PreferenceConstants.MELTI_ROLE_STUDENT }, {
				"Profesor", PreferenceConstants.MELTI_ROLE_PROFESOR }
		}, getFieldEditorParent());
		idPrefEditor = new StringFieldEditor(PreferenceConstants.MELTI_USERID, "ID Usuario", getFieldEditorParent());
		keyPrefEditor = new StringFieldEditor(PreferenceConstants.MELTI_KEY, "Key", getFieldEditorParent());
		secretPrefEditor = new StringFieldEditor(PreferenceConstants.MELTI_SECRET, "Secret", getFieldEditorParent());
		tokenPrefEditor = new StringFieldEditor(PreferenceConstants.MELTI_TOKEN, "Token", getFieldEditorParent());
		addField(rolePrefEditor);
		addField(idPrefEditor);
		addField(keyPrefEditor);
		addField(secretPrefEditor);
		addField(tokenPrefEditor);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(MeltiPlugin.getDefault().getPreferenceStore());
		//setDescription("Configuración");
	}
	
	/* Si existen cambios en las preferencias se avisa del evento 
	 * para checkear el estado 
	 */
	/*@Override
	public void propertyChange(PropertyChangeEvent event) {
		//TODO comprobar que se introduzca un número en el puerto
		//TODO formato correcto del host por ejemplo...
		//TODO probar todas las opciones posibles
		super.propertyChange(event);
	    if (event.getProperty().equals(FieldEditor.VALUE)) {
	    	if (event.getSource() == serverPrefEditor)
	    		checkState();
	    }
	}*/
	
	// Se comprueba el estado de las preferencias
	@Override
	protected void checkState() {
		super.checkState();
		if (idPrefEditor.getStringValue()!=null && !idPrefEditor.getStringValue().equals("")) {
			setErrorMessage(null);
			setValid(true);
		} else {
			setErrorMessage("Error: El campo ID usuario no puede estar vacío");
			setValid(false);
		}
		
	}
	
	/*public boolean performOk() {
		this.storeValues();
		//return true;
		return super.performOk();
	}*/
	
	/* 
	 * Guardamos todos los valores de las preferencias
	 * para poder usarlos en diferentes sesiones 
	 */
	/*private void storeValues() {
		serverPrefEditor.store();
		portPrefEditor.store();
		modeRolePrefEditor.store();
		IDPrefEditor.store();
	}*/
	
}