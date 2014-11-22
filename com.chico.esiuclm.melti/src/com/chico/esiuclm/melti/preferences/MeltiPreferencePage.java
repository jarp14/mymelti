package com.chico.esiuclm.melti.preferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	    	if (event.getSource() == idPrefEditor) {
	    		checkState(0);
	    	} else if (event.getSource() == keyPrefEditor) {
	    		checkState(1);
	    	} else if (event.getSource() == secretPrefEditor) {
	    		checkState(2);
	    	}
	    }
	}
	
	// Se comprueba el estado de las preferencias
	protected void checkState(int editor) {
		super.checkState();
		
		switch(editor) {
			case 0:
				if (idPrefEditor.getStringValue()!=null && !idPrefEditor.getStringValue().equals("")) {
					if(validateEmail(idPrefEditor.getStringValue())) {
						setErrorMessage(null);
						setValid(true);
					} else {
						setErrorMessage("Aviso: ID usuario no es un email válido");
						setValid(false);
					}
				} else {
					setErrorMessage("Aviso: ID usuario no puede estar vacío");
					setValid(false);
				}
				break;
			case 1:
				if (keyPrefEditor.getStringValue()!=null && !keyPrefEditor.getStringValue().equals("")) {
					setErrorMessage(null);
					setValid(true);
				} else {
					setErrorMessage("Aviso: Key no puede estar vacío");
					setValid(false);
				}
				break;
			case 2:
				if (secretPrefEditor.getStringValue()!=null && !secretPrefEditor.getStringValue().equals("")) {
					setErrorMessage(null);
					setValid(true);
				} else {
					setErrorMessage("Aviso: Secret no puede estar vacío");
					setValid(false);
				}
				break;
				
			default:
				break;
		}		
	}
	
	// Comprueba si el String introducido es un email valido
	private boolean validateEmail(String email) {
		String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		// Compila la expresion regular en un patron
		Pattern pattern = Pattern.compile(PATTERN_EMAIL);
		// Comprueba el patron sobre el email
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}
	
}