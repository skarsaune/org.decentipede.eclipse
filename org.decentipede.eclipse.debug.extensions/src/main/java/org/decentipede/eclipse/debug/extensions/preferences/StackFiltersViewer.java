package org.decentipede.eclipse.debug.extensions.preferences;

import java.io.IOException;

import no.kantega.decentipede.debug.extensions.action.FrameFilterRepository;

import org.decentipede.eclipse.debug.core.DecentipedePlugin;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

class StackFiltersViewer extends ListEditor {
	private Composite parent;

	StackFiltersViewer(String name, String labelText,
			final Composite parent) {
		super(name, labelText, parent);
		this.parent=parent;
		getList().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				int selectionIndex = getList().getSelectionIndex();
				if(selectionIndex > -1) {
					final String name = getList().getItem(selectionIndex);
					//open edit view on existing selection
					new ManagePatternDialog(parent, name);
				}
			}
		});
	}
	
	private FrameFilterRepository filterRepo() {
		return DecentipedePlugin.getDefault().getFrameFilterRepository();
	}

	@Override
	protected String createList(String[] items) {
		filterRepo().ensureFilters(items);
		
		try {
			return filterRepo().asPropertyString();
		} catch (IOException e) {
			return "Error serializing as string, should never happen :" + e;
		}
	}



	@Override
	protected String getNewInputObject() {
		return new ManagePatternDialog(this.parent).getResultingName();
	}

	@Override
	protected String[] parseString(String stringList) {
		return filterRepo().filterNames();
	}
}