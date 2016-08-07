package org.decentipede.eclipse.debug.extensions.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ManageStackFilters extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println(event);
		return null;
	}


}