package org.decentipede.eclipse.debug.extensions.popup;

import org.decentipede.debug.extensions.action.FrameFilterRepository;
import org.decentipede.debug.extensions.action.ToggleableFilter;
import org.decentipede.eclipse.debug.core.DecentipedePlugin;
import org.decentipede.eclipse.debug.extensions.preferences.PreferenceConstants;
import org.decentipede.eclipse.debug.services.IFrameFilter;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

@SuppressWarnings("restriction")
public class StackFiltersDynamicMenuBuilder extends
		ExtensionContributionFactory {

	public StackFiltersDynamicMenuBuilder() {
	}

	@Override
	public void createContributionItems(IServiceLocator serviceLocator,
			IContributionRoot additions) {
		
		final IFrameFilter repository = (IFrameFilter) serviceLocator.getService(IFrameFilter.class);

		
		
		additions.addContributionItem(new ContributionItem() {
			@Override
			public void fill(Menu menu, int index) {
				final MenuItem cascade=new MenuItem(menu, SWT.CASCADE);
				cascade.setText("Stack Frame Filters");
				final Menu submenu = new Menu(menu);
				cascade.setMenu(submenu);
						

				

				for (final ToggleableFilter filter : repository.allFilters()) {
					final MenuItem filterItem = new MenuItem(submenu, SWT.CHECK);
					filterItem.addSelectionListener(filter);
					filterItem.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							// Selection would typically toggle selection, therefore ensure it is reflected in viewer
							repository.forceSync();
						}
					});
					filterItem.setText(filter.name());
					filterItem.setSelection(filter.isFilterActive());
				}
				
				new MenuItem(submenu, SWT.SEPARATOR);
				MenuItem editFilters = new MenuItem(submenu, SWT.PUSH);
				editFilters.setText("Edit filters ...");
				editFilters.addSelectionListener(new SelectionAdapter() {
					@SuppressWarnings("restriction")
					@Override
					public void widgetSelected(SelectionEvent e) {
						SWTFactory.showPreferencePage(PreferenceConstants.PREFERENCE_PAGE);
					}
				});
				
				
			}
		}, new Expression(){

			@Override
			public EvaluationResult evaluate(IEvaluationContext context)
					throws CoreException {
				return repository.isSelectionRelevant() ? EvaluationResult.TRUE : EvaluationResult.FALSE;
			}
			
		});

	}

}
