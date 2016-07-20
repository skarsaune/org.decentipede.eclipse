/*******************************************************************************
 * Copyright (c) 2006, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package no.kantega.decentipede.debug.extensions.action;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.ui.IDebugView;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.actions.ObjectActionDelegate;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Base class for inspectors for objects
 */
public abstract class ExtendedObjectInspector extends ObjectActionDelegate
		implements IWorkbenchWindowActionDelegate {

	protected IWorkbenchWindow fWindow;

	@SuppressWarnings("restriction")
	public void run(IAction action) {

		IStructuredSelection currentSelection = getCurrentSelection();
		if (isJavaObject(currentSelection)) {
			try {
				inspectObject(currentSelection);
			} catch (DebugException e) {
				JDIDebugUIPlugin.statusDialog(e.getStatus());
			}

		} else {
			JDIDebugUIPlugin.statusDialog(new Status(IStatus.WARNING,
					JDIDebugUIPlugin.getUniqueIdentifier(),
					"An object must be selected to inspect attribute"));
		}
	}

	protected abstract void inspectObject(IStructuredSelection currentSelection)
			throws DebugException;

	/**
	 * Compute an anchor based on selected item in the tree.
	 * 
	 * @param view
	 *            anchor view
	 * @return anchor point
	 */
	protected static Point getAnchor(IDebugView view) {
		Control control = view.getViewer().getControl();
		if (control instanceof Tree) {
			Tree tree = (Tree) control;
			TreeItem[] selection = tree.getSelection();
			if (selection.length > 0) {
				Rectangle bounds = selection[0].getBounds();
				return tree.toDisplay(new Point(bounds.x, bounds.y
						+ bounds.height));
			}
		}
		return control.toDisplay(0, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 * IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		fWindow = window;
	}

	/**
	 * @return the shell to use for new popups or <code>null</code>
	 */
	protected Shell getShell() {
		if (fWindow != null) {
			return fWindow.getShell();
		}
		if (getWorkbenchWindow() != null) {
			return getWorkbenchWindow().getShell();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.internal.debug.ui.actions.ObjectActionDelegate#getPart()
	 */
	@Override
	protected IWorkbenchPart getPart() {
		IWorkbenchPart part = super.getPart();
		if (part != null) {
			return part;
		} else if (fWindow != null) {
			return fWindow.getActivePage().getActivePart();
		}
		return null;
	}

	@Override
	public void selectionChanged(IAction action, ISelection sel) {

		action.setEnabled(isJavaObject(sel));

	}

	protected boolean isJavaObject(ISelection sel) {
		return getSelectedType(sel) instanceof IJavaClassType;
	}

	protected IJavaType getSelectedType(ISelection sel) {
		IJavaVariable element = getVariable(sel);
		if (element != null) {
			try {
				return element.getJavaType();
			} catch (DebugException e) {
				return null;
			}
		}

		return null;
	}

	protected IJavaVariable getVariable(ISelection sel) {
		if (sel instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) sel).getFirstElement();
			if (element instanceof IJavaVariable) {
				return (IJavaVariable) element;
			}
		}
		return null;
	}

}
