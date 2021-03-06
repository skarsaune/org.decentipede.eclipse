package org.decentipede.debug.extensions.action;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.ui.IDebugView;
import org.eclipse.debug.ui.InspectPopupDialog;
import org.eclipse.jdt.debug.core.IJavaReferenceType;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jdt.internal.debug.ui.actions.PopupInspectAction;
import org.eclipse.jdt.internal.debug.ui.display.JavaInspectExpression;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Show the classloader of selected object
 * 
 */
@SuppressWarnings("restriction")
public class InspectClassLoaderActionDelegate extends ExtendedObjectInspector
		implements IWorkbenchWindowActionDelegate {

	@Override
	protected void inspectObject(IStructuredSelection currentSelection)
			throws DebugException {

		IJavaVariable var = getVariable(currentSelection);
		InspectPopupDialog ipd = new InspectPopupDialog(getShell(),
				getAnchor((IDebugView) getPart().getAdapter(IDebugView.class)),
				PopupInspectAction.ACTION_DEFININITION_ID,
				new JavaInspectExpression(var.getName() + " classloader:",
						((IJavaReferenceType) var.getJavaType())
								.getClassLoaderObject()));
		ipd.open();

	}

}
