package org.decentipede.eclipse.debug.extensions.popup;

import java.util.Collections;

import org.decentipede.eclipse.debug.core.DecentipedePlugin;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.IWorkbenchContribution;
import org.eclipse.ui.services.IServiceLocator;

public class StackFiltersDynamicMenu extends CompoundContributionItem implements IWorkbenchContribution {

	private IServiceLocator serviceLocator;

	public StackFiltersDynamicMenu() {
	}

	@Override
	protected IContributionItem[] getContributionItems() {
		
		final String[] filterNames = DecentipedePlugin.getDefault()
				.getFrameFilterRepository().filterNames();
				
		final IContributionItem[] items = new IContributionItem[filterNames.length];
		for(int i=0;i<filterNames.length; i++) {
			final String filterName = filterNames[i];
			CommandContributionItemParameter p = new CommandContributionItemParameter(
					serviceLocator, "org.decentipede.eclipse.debug.extensions.filters." + filterName,
					"org.decentipede.eclipse.debug.extensions.StackFilter",
					CommandContributionItem.STYLE_CHECK);
			p.label = filterName;
			p.parameters = Collections.singletonMap("org.decentipede.eclipse.debug.extensions.filterName", filterName);

			CommandContributionItem item = new CommandContributionItem(p);
			
			item.setVisible(true);
			
			items[i] = item;
		}
		return items;
	}

	@Override
	public void initialize(IServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
		
	}

}
