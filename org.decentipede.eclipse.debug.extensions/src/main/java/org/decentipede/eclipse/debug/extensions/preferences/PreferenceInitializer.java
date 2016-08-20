package org.decentipede.eclipse.debug.extensions.preferences;

import java.io.IOException;

import org.decentipede.debug.extensions.action.FrameFilterRepository;
import org.decentipede.eclipse.debug.core.DecentipedePlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = DecentipedePlugin.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_ANALYZE_NULLPOINTERS, false);
		store.setDefault(PreferenceConstants.P_FILTER_NATIVE_FRAMES, false);
		store.setDefault(PreferenceConstants.P_FILTER_SYNTETHIC_FRAMES, false);
		try {
			store.setDefault(PreferenceConstants.P_STACK_FILTERS, FrameFilterRepository.factoryDefaults().asPropertyString());
			//ensure persistent representation
			((IPersistentPreferenceStore) store).save();
		} catch (IOException ignore) {
		}
	}

}
