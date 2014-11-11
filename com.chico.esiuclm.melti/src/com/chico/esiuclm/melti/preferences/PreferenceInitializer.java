package com.chico.esiuclm.melti.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.chico.esiuclm.melti.MeltiPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public PreferenceInitializer() {}
	
	/*
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 * Valores por defecto en la página de preferencias
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = MeltiPlugin.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.MELTI_ROLE, PreferenceConstants.MELTI_ROLE_PROFESOR);
		store.setDefault(PreferenceConstants.MELTI_USERID, "usuario@uclm.es");
		store.setDefault(PreferenceConstants.MELTI_TOKEN, "XXXXXXXXXXXXXX");
		store.setDefault(PreferenceConstants.MELTI_KEY, "java");
		store.setDefault(PreferenceConstants.MELTI_SECRET, "chico.esiuclm.melti");
		//store.setDefault(PreferenceConstants.MELTI_SERVER, "localhost");
		//store.setDefault(PreferenceConstants.MELTI_PORT, "8080");
	}

}
