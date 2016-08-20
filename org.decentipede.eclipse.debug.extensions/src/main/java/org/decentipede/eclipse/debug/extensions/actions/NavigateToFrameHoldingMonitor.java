package org.decentipede.eclipse.debug.extensions.actions;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.internal.ui.views.launch.LaunchView;
import org.eclipse.debug.ui.IDebugView;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.internal.debug.core.model.JDIThread;
import org.eclipse.jdt.internal.debug.ui.actions.ObjectActionDelegate;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IWorkbenchPart;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.MonitorInfo;

@SuppressWarnings("restriction")
public class NavigateToFrameHoldingMonitor extends ObjectActionDelegate {

	private Viewer viewer;
	private IJavaThread selectedThread;

	@Override
	public void run(IAction action) {
		try {
			if(this.selectedThread != null) {
				final IJavaObject inContention=this.selectedThread.getContendedMonitor();
				if(inContention != null) {
					final IJavaThread culprit = inContention.getOwningThread();
					if(culprit instanceof JDIThread) {
						for (final MonitorInfo monitorInfo : ((JDIThread)culprit).getUnderlyingThread().ownedMonitorsAndFrames()) {
							if(monitorInfo.monitor().uniqueID() == inContention.getUniqueId()){
								if(culprit != null && culprit.getFrameCount() > monitorInfo.stackDepth()) {
									final IStackFrame culpritFrame = culprit.getStackFrames()[monitorInfo.stackDepth()];
									viewer.getControl().getDisplay().asyncExec(new Runnable() {
										
										@Override
										public void run() {
											viewer.setSelection(new StructuredSelection(culpritFrame), true);
										}
									});
									
								}
							}
								
						}
					}
					
					
				}
				
			}
		} catch (DebugException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncompatibleThreadStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection sel) {
		action.setEnabled(isThreadWaitingForMonitor(sel));
		
	}
	
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		if(targetPart instanceof LaunchView) {
			this.viewer=((IDebugView) targetPart).getViewer();
		}

	}

	private boolean isThreadWaitingForMonitor(ISelection sel) {
		if(sel instanceof IStructuredSelection) {
			Object selected = ((IStructuredSelection)sel).getFirstElement();
			if(selected instanceof IJavaThread) {
				this.selectedThread = (IJavaThread)selected;
				try {
					return this.selectedThread.getContendedMonitor() != null;
				} catch (DebugException e) {
					return false;
				}
			}
			
		}
		return false;
	}
	
	

}
