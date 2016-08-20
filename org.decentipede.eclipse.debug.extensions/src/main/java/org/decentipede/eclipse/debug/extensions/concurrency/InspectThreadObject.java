package org.decentipede.eclipse.debug.extensions.concurrency;

import org.decentipede.debug.extensions.action.ExtendedObjectInspector;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.ui.IDebugView;
import org.eclipse.debug.ui.InspectPopupDialog;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.internal.debug.ui.actions.PopupInspectAction;
import org.eclipse.jdt.internal.debug.ui.display.JavaInspectExpression;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

@SuppressWarnings("restriction")
public class InspectThreadObject extends ExtendedObjectInspector implements
		IWorkbenchWindowActionDelegate {

	@Override
	protected void inspectObject(IStructuredSelection currentSelection)
			throws DebugException {

		IJavaThread thread = (IJavaThread) currentSelection.getFirstElement();

		InspectPopupDialog ipd = new InspectPopupDialog(getShell(),
				getAnchor((IDebugView) getPart().getAdapter(IDebugView.class)),
				PopupInspectAction.ACTION_DEFININITION_ID,
				new JavaInspectExpression(thread.getName() + " classloader:",
						thread.getThreadObject()));
		ipd.open();
	}

	@Override
	protected boolean isValidSelection(ISelection sel) {

		return sel instanceof IStructuredSelection
				&& ((IStructuredSelection) sel).getFirstElement() instanceof IJavaThread;
	}
}
