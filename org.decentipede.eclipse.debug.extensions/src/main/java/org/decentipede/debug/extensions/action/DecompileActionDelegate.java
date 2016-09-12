package org.decentipede.debug.extensions.action;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;

import no.kantega.debug.decompile.ClassFileReverseEnginerer;

import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.internal.debug.ui.actions.ObjectActionDelegate;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jetbrains.java.decompiler.main.DecompilerContext;

import com.sun.jdi.Location;

public abstract class DecompileActionDelegate extends ObjectActionDelegate
implements IWorkbenchWindowActionDelegate {

	protected Location fetchLocation;

	@SuppressWarnings("unchecked")
	@Override
	public void run(IAction action) {
		if(this.fetchLocation != null) {
			try {
				DecompilerContext.initContext(Collections.EMPTY_MAP);
				System.out.println(decompile());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}



	protected abstract String decompile() throws Exception;



	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.fetchLocation = fetchLocation(selection);
		action.setEnabled(fetchLocation != null && fetchLocation.virtualMachine().canGetBytecodes() && fetchLocation.virtualMachine().canGetConstantPool());
		
	}

	private Location fetchLocation(ISelection selection) {
		if(!(selection instanceof IStructuredSelection)) {
			return null;
		}
		Object selectionObject = ((IStructuredSelection)selection).getFirstElement();
		if(!(selectionObject instanceof IJavaStackFrame)) {
			return null;
		}
		try {
			Field locationField = selectionObject.getClass().getDeclaredField("fLocation");
			locationField.setAccessible(true);
			return (Location)locationField.get(selectionObject);
			
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void init(IWorkbenchWindow window) {
		
	}

}
