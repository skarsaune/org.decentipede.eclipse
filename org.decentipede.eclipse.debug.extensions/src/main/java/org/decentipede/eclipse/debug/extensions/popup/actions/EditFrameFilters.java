package org.decentipede.eclipse.debug.extensions.popup.actions;

import org.decentipede.eclipse.debug.extensions.preferences.PreferenceConstants;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;

/**
 * I open the Decentipede preference page to allow specifying frame filters
 * @author marska
 *
 */
@SuppressWarnings("restriction")
public class EditFrameFilters implements IActionDelegate {

	@Override
	public void run(IAction paramIAction) {
		SWTFactory.showPreferencePage(PreferenceConstants.PREFERENCE_PAGE);		
	}

	@Override
	public void selectionChanged(IAction paramIAction,
			ISelection paramISelection) {
		//always available
	}

}
