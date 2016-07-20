package org.decentipede.eclipse.debug.core;

import no.kantega.decentipede.debug.extensions.action.FrameFilterRepository;

import org.decentipede.eclipse.debug.extensions.preferences.PreferenceConstants;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class DecentipedePlugin {
	
	private static DecentipedePlugin plugin;
	
    private ScopedPreferenceStore preferenceStore;

	private FrameFilterRepository repository;
	
	public static DecentipedePlugin getDefault() {
		if(plugin == null) {
			plugin = new DecentipedePlugin();
		}
		return plugin;
	}

	public IPreferenceStore getPreferenceStore() {
        // Create the preference store lazily.
        if (preferenceStore == null) {
            preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE,"Decentipede");

        }
        return preferenceStore;
	}
	
	public FrameFilterRepository getFrameFilterRepository() {
		if(this.repository!= null) {
			return this.repository;
		}
		this.repository = FrameFilterRepository.factoryDefaults();
		try {
			repository.mergeWith(FrameFilterRepository.fromPropertyString(getPreferenceStore().getString(PreferenceConstants.P_STACK_FILTERS)));
		} catch (Exception e) {
		}
		return repository;
		

	}

}
