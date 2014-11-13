package com.chico.esiuclm.melti.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.chico.esiuclm.melti.MeltiPlugin;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public PreferenceInitializer() {}
	
	/**
	 * Valores por defecto en la página de preferencias
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = MeltiPlugin.getDefault().getPreferenceStore();
		store.setDefault(IPreferenceConstants.MELTI_ROLE, IPreferenceConstants.MELTI_ROLE_STUDENT);
		store.setDefault(IPreferenceConstants.MELTI_USERID, "usuario@uclm.es");
		store.setDefault(IPreferenceConstants.MELTI_TOKEN, "pega aquí el token de tu espacio en Moodle");
		store.setDefault(IPreferenceConstants.MELTI_KEY, "java");
		store.setDefault(IPreferenceConstants.MELTI_SECRET, "chico.esiuclm.melti");
	}

}
