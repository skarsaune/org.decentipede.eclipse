package org.decentipede.eclipse.debug.extensions.actions.structure;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaReferenceType;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ShowClassLoader implements IObjectActionDelegate{

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if(selection instanceof IJavaObject) {
			IJavaType javaType;
			try {
				javaType = ((IJavaObject) selection).getJavaType();
				if(javaType instanceof IJavaReferenceType) {
//					InspectPopupDialog ipd = new InspectPopupDialog(getShell(), 
//							getAnchor((IDebugView) getPart().getAdapter(IDebugView.class)), 
//							PopupInspectAction.ACTION_DEFININITION_ID,
//							new JavaInspectExpression(var.getName() + " classloader:",((IJavaReferenceType) var).getClassLoaderObject()));
//					ipd.open();
//					((IJavaReferenceType) javaType).getClassLoaderObject();
				}
			} catch (DebugException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

}
