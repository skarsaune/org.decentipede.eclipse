package org.decentipede.eclipse.debug.extensions.popup.actions;

import org.eclipse.debug.ui.IDebugView;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public abstract class ToggleFilterAction implements IObjectActionDelegate{
	private StructuredViewer viewer;
	protected boolean filterActive;
	private final ViewerFilter filter=makeFilter();

	@Override
	public void run(IAction paramIAction) {
		filterActive = !filterActive;
		paramIAction.setChecked(filterActive);
		this.viewer.refresh();
	}

	abstract ViewerFilter makeFilter();

	@Override
	public void selectionChanged(IAction paramIAction,
			ISelection paramISelection) {		
	}

	@Override
	public void setActivePart(IAction paramIAction,
			IWorkbenchPart targetPart) {
		if(targetPart instanceof IDebugView && targetPart.getClass().getSimpleName().equals("LaunchView"))
		{
			final Viewer partViewer = ((IDebugView)targetPart).getViewer();
			if(partViewer instanceof StructuredViewer)
			{
				this.viewer = (StructuredViewer) partViewer;
				addMyFilter(this.viewer);
			}
			
		}		
	}

	private void addMyFilter(final StructuredViewer viewer) {
		//check if for some reason filter has already been added
		for (ViewerFilter viewerFilter : viewer.getFilters()) {
			if(viewerFilter==this.filter) {
				return;
			}
		}
		
		viewer.addFilter(this.filter);
		
	}
}
