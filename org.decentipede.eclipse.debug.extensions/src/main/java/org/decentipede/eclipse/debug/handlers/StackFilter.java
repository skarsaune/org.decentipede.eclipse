package org.decentipede.eclipse.debug.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

public class StackFilter implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		System.out.println(handlerListener);

	}

	@Override
	public void dispose() {
		System.out.println("dispose");
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println(event);
		return null;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		System.out.println(handlerListener);

	}

}
