package org.decentipede.eclipse.debug.extensions.properties;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

@SuppressWarnings("restriction")
public class ProcessExtendedPropertyPage extends PropertyPage {
	/**
	 * 
	 * @return PID as string or error message
	 */
	private String pidString() {
		try {
			final Integer pidAttempt = this.attemptToGrabPid();
			if (pidAttempt != null) {
				return String.valueOf(pidAttempt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Could not detect process pid";
	}

	/**
	 * Use reflection to traverse the underlying objects from Eclipse.
	 * 
	 * NB: Best effort based, will break if underlying Eclipse structure changes
	 * 
	 * @return PID value or null
	 */
	private Integer attemptToGrabPid() throws NoSuchFieldException,
			IllegalAccessException {

		Object adapted = getElement().getAdapter(IProcess.class);
		if (adapted instanceof IProcess) {
			Field actualProcessField = adapted.getClass().getDeclaredField(
					"fProcess");

			actualProcessField.setAccessible(true);
			final Object process = actualProcessField.get(adapted);
			if (process instanceof Process) {
				final Field pidField = process.getClass().getDeclaredField(
						"pid");
				pidField.setAccessible(true);
				return (Integer) pidField.get(process);

			}

		}
		return null;
	}

	private String printPropertiesSorted(final Object properties) {
		@SuppressWarnings("unchecked")
		final Map<String, String> sorted = new TreeMap<String, String>(
				(Map<String, String>) properties);
		final StringBuilder builder = new StringBuilder();
		for (Entry<String, String> sysProp : sorted.entrySet()) {
			builder.append(sysProp.getKey()).append(" -> ")
					.append(sysProp.getValue()).append("\n");
		}
		return builder.toString();
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite ancestor) {

		noDefaultAndApplyButton();
		Composite parent = SWTFactory.createComposite(ancestor,
				ancestor.getFont(), 1, 1, GridData.FILL_BOTH);

		// create the process time section
		SWTFactory.createLabel(
				parent,
				"Process Identifier (PID):",
				JFaceResources.getFontRegistry().getBold(
						JFaceResources.DIALOG_FONT), 1);
		Text pidText = SWTFactory.createText(parent, SWT.READ_ONLY, 1);
		pidText.setBackground(ancestor.getBackground());
		((GridData) pidText.getLayoutData()).horizontalIndent = 10;
		pidText.setText(pidString());

		// create environment section
		SWTFactory.createLabel(parent, "System Properties:", JFaceResources
				.getFontRegistry().getBold(JFaceResources.DIALOG_FONT), 1);
		Text sysPropText = SWTFactory.createText(parent, SWT.H_SCROLL
				| SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL, 1,
				convertWidthInCharsToPixels(13),
				convertHeightInCharsToPixels(8), GridData.FILL_BOTH);
		sysPropText.setBackground(parent.getBackground());
		((GridData) sysPropText.getLayoutData()).horizontalIndent = 10;

		// Populate owner text field
		sysPropText.setText(attemptToReadSystemProperties());

		return parent;

	}

	/**
	 * Try to attach to vm by attach api.
	 * Use reflection to detect whether classes are available in the underlying VM
	 * 
	 * @return
	 */
	private String attemptToReadSystemProperties() {
		try {

			Integer pid = this.attemptToGrabPid();
			if (pid != null) {
				Class<?> attachMechanism = Class
						.forName("com.sun.tools.attach.VirtualMachine");
				Method attachMethod = attachMechanism.getDeclaredMethod(
						"attach", String.class);
				Object vm = attachMethod.invoke(null, pid.toString());
				Method sysPropMethod = vm.getClass().getMethod(
						"getSystemProperties");
				return printPropertiesSorted(sysPropMethod.invoke(vm));
			}

		} catch (Exception e) {
		}
		return "Could not retrieve System Properties";
	}

}