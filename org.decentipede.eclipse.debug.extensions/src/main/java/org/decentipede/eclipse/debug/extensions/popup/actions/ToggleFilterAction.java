package org.decentipede.eclipse.debug.extensions.popup.actions;

import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IObjectActionDelegate;

public abstract class ToggleFilterAction implements IObjectActionDelegate , SelectionListener{
	private StructuredViewer viewer;


	@Override
	public void run(IAction paramIAction) {
		toggleFilter();
		this.viewer.refresh();
	}


	
	public abstract String name();

	@Override
	public void selectionChanged(IAction paramIAction,
			ISelection paramISelection) {		
	}




	
	public abstract boolean isFilterActive();
	
	@Override
	public void widgetSelected(SelectionEvent e){
		if(e.widget instanceof MenuItem) {
			((MenuItem)e.widget).setSelection(this.toggleFilter());
		}
	}

	abstract boolean toggleFilter(); 
	
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {		
	}



	public abstract boolean filter(IJavaStackFrame node, String stackFrameText);





}
