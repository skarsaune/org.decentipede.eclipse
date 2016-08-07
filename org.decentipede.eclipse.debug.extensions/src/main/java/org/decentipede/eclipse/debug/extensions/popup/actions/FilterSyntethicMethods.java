package org.decentipede.eclipse.debug.extensions.popup.actions;

import no.kantega.decentipede.debug.extensions.action.ToggleableFilter;

import org.decentipede.eclipse.debug.core.DecentipedePlugin;
import org.decentipede.eclipse.debug.extensions.preferences.PreferenceConstants;
import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;

/**
 * I filter out syntethic methods from the stack frame viewer
 * 
 * @author marska
 *
 */
@SuppressWarnings("restriction")
public class FilterSyntethicMethods extends ToggleableFilter {

	public FilterSyntethicMethods() {
		super();
	}

	@Override
	public boolean filter(IJavaStackFrame frame, String asString) {

		if (isFilterActive() && frame instanceof JDIStackFrame) {
			
			if(((JDIStackFrame) frame).getUnderlyingMethod().isSynthetic()) {
				try {
					return frame.getMethodName().contains("lambda");
				} catch (DebugException e) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isFilterActive() {
		return DecentipedePlugin.getDefault().getPreferenceStore()
				.getBoolean(PreferenceConstants.P_FILTER_SYNTETHIC_FRAMES);
	}

	@Override
	public boolean toggleFilter() {
		boolean toggled = !isFilterActive();
		DecentipedePlugin
				.getDefault()
				.getPreferenceStore()
				.setValue(PreferenceConstants.P_FILTER_SYNTETHIC_FRAMES,
						toggled);
		return toggled;
	}

	@Override
	public String name() {
		return "syntethic methods (except lambda methods)";
	}

}
