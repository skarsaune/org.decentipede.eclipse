package org.decentipede.eclipse.debug.core;

import org.decentipede.debug.extensions.action.FrameFilterRepository;
import org.decentipede.eclipse.debug.extensions.preferences.PreferenceConstants;
import org.decentipede.eclipse.debug.services.IFrameFilter;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

public class DecentipedePlugin extends Plugin {
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
				
		context.getServiceReference(IFrameFilter.class);
		//ensure that filter service is started and hooked up to debug view
		PlatformUI.getWorkbench().getService(ISelectionService.class);
	}
	
	private static DecentipedePlugin plugin;
	
    private ScopedPreferenceStore preferenceStore;

	private FrameFilterRepository repository;
	
	public static DecentipedePlugin getDefault() {
		if(plugin == null) {
			plugin = new DecentipedePlugin();
		}
		return plugin;
	}

	public IPersistentPreferenceStore getPreferenceStore() {
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
