package org.decentipede.eclipse.debug.extensions.popup;

import java.util.Collections;

import org.decentipede.eclipse.debug.core.DecentipedePlugin;
import org.eclipse.core.expressions.Expression;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

public class StackFiltersDynamicMenuBuilder extends
		ExtensionContributionFactory {

	public StackFiltersDynamicMenuBuilder() {
	}

	@Override
	public void createContributionItems(IServiceLocator serviceLocator,
			IContributionRoot additions) {
		
		additions.addContributionItem(new ContributionItem() {
			@Override
			public void fill(Menu menu, int index) {
				final MenuItem cascade=new MenuItem(menu, SWT.CASCADE);
				cascade.setText("Stack Frame Filters");
				final Menu submenu = new Menu(menu);
				cascade.setMenu(submenu);
						

				for (String filterName : DecentipedePlugin.getDefault()
						.getFrameFilterRepository().filterNames()) {
					final MenuItem filterItem = new MenuItem(submenu, SWT.CHECK);
					filterItem.setText(filterName);
				}
				
			}
		}, Expression.TRUE);

	}

}
