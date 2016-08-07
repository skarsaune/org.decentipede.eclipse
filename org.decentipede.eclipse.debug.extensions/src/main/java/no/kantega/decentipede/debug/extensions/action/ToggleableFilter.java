package no.kantega.decentipede.debug.extensions.action;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.MenuItem;

public abstract class ToggleableFilter implements SelectionListener , FrameFilter {

	protected boolean filterActive;


	@Override
	public void widgetSelected(SelectionEvent e){
		if(e.widget instanceof MenuItem) {
			((MenuItem)e.widget).setSelection(this.toggleFilter());
		}
	}

	

	public boolean isFilterActive() {
		return this.filterActive;
	}




	public boolean toggleFilter() {
		this.filterActive = ! this.filterActive;
		return this.filterActive;
	}



	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// ignore
	}



	public abstract String name();

}
