package org.decentipede.eclipse.debug.extensions.popup.actions;

import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
/**
 * I filter out syntethic methods from the stack frame viewer
 * @author marska
 *
 */
@SuppressWarnings("restriction")
public class FilterSyntethicMethods extends ToggleFilterAction {
	
	public FilterSyntethicMethods() {
		super();
	}

	@Override
	ViewerFilter makeFilter() {
		return new ViewerFilter() {
			
			@Override
			public boolean select(Viewer paramViewer, Object path,
					Object node) {
				if(node instanceof JDIStackFrame) {
					return !((JDIStackFrame) node).getUnderlyingMethod().isSynthetic();
				}
				return true;
			}
		};	
	}
}
