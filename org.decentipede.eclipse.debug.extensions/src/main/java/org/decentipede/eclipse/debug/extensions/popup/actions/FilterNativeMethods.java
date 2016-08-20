package org.decentipede.eclipse.debug.extensions.popup.actions;

import org.decentipede.debug.extensions.action.ToggleableFilter;
import org.decentipede.eclipse.debug.core.DecentipedePlugin;
import org.decentipede.eclipse.debug.extensions.preferences.PreferenceConstants;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;

/**
 * I filter out syntethic methods from the stack frame viewer
 * 
 * @author marska
 *
 */
@SuppressWarnings("restriction")
public class FilterNativeMethods extends ToggleableFilter {

	public FilterNativeMethods() {
		super();
	}

	@Override
	public boolean filter(IJavaStackFrame frame, String asString) {

		if (isFilterActive() && frame instanceof JDIStackFrame) {
			
			return !((JDIStackFrame) frame).getUnderlyingMethod().isNative();
		}
		return true;
	}

	@Override
	public boolean isFilterActive() {
		return DecentipedePlugin.getDefault().getPreferenceStore()
				.getBoolean(PreferenceConstants.P_FILTER_NATIVE_FRAMES);
	}

	@Override
	public boolean toggleFilter() {
		boolean toggled = !isFilterActive();
		DecentipedePlugin
				.getDefault()
				.getPreferenceStore()
				.setValue(PreferenceConstants.P_FILTER_NATIVE_FRAMES,
						toggled);
		return toggled;
	}

	@Override
	public String name() {
		return "native methods";
	}

}
