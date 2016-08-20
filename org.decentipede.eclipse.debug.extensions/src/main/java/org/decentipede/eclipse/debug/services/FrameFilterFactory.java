package org.decentipede.eclipse.debug.services;

import org.decentipede.debug.extensions.action.FrameFilterRepository;
import org.decentipede.eclipse.debug.core.DecentipedePlugin;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;


public class FrameFilterFactory extends AbstractServiceFactory {

	@Override
	public Object create(@SuppressWarnings("rawtypes") Class serviceInterface, IServiceLocator parentLocator,
			IServiceLocator locator) {
		
		if(serviceInterface == IFrameFilter.class) {
			ISelectionService service = (ISelectionService) locator.getService(org.eclipse.ui.ISelectionService.class);
			//service singleton is in fact accessible from plugin
			final FrameFilterRepository repository = DecentipedePlugin.getDefault().getFrameFilterRepository();
			//make sure we do not register more than once
			service.removeSelectionListener(FrameFilterRepository.partIdOfConcern(), repository);
			service.addSelectionListener(FrameFilterRepository.partIdOfConcern(), repository);

			return repository;
			
		} 
		return null;
		
	}

}
