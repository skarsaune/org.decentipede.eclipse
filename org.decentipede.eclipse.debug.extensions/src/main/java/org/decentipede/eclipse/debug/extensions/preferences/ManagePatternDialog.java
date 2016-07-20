package org.decentipede.eclipse.debug.extensions.preferences;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import no.kantega.decentipede.debug.extensions.action.FrameFilterRepository;
import no.kantega.decentipede.debug.extensions.action.FrameFilterSet;

import org.decentipede.eclipse.debug.core.DecentipedePlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ManagePatternDialog {

	private String resultingName;
	private final CLabel errorLabel;
	private final Shell shell;
	private final Text nameText;
	private final Text patterns;
	private final Control parentControl;

	ManagePatternDialog(final Control parent, final String existingName) {

		this.parentControl = parent;
		this.shell = new Shell(parent.getShell(), SWT.PRIMARY_MODAL | SWT.TITLE
				| SWT.DIALOG_TRIM | SWT.ON_TOP);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		final CLabel nameLabel = new CLabel(shell, SWT.NONE);
		nameLabel.setText("Filter name:");
		this.nameText = new Text(shell, SWT.SINGLE);
		final CLabel patternLabel = new CLabel(shell, SWT.NONE);
		patternLabel
				.setText("Enter regular expression patterns for frames to filter on each line");
		this.patterns = new Text(shell, SWT.MULTI);
		this.patterns
				.setToolTipText("A match for any of the patterns \nwill mean that any stack frame that matches it will be excluded, \npatterns should therefore be very specific and restrictive");
		this.errorLabel = new CLabel(shell, SWT.SINGLE);

		final Button button = new Button(shell, SWT.PUSH);
		button.setText("Save");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				save();
			}
		});

		loadFromExisting(existingName);
		open();

	}

	public ManagePatternDialog(Control parentControl) {
		this(parentControl, null);
	}

	private void loadFromExisting(final String existingName) {
		if (existingName == null
				|| filterRepo().getFilter(existingName) == null) {
			this.shell.setText("Add new filter set");
			return;
		}
		final FrameFilterSet filter = filterRepo().getFilter(existingName);
		this.shell.setText("Edit filter " + existingName);
		this.nameText.setText(existingName);
		this.patterns.setText(filter.delimitedString().replaceAll(
				FrameFilterSet.DELIMITER, "\n"));

	}

	private void open() {
		shell.pack();
		Shell parentShell = parentControl.getShell();
		shell.setLocation(parentShell.getLocation().x
				+ ((parentShell.getSize().x - shell.getSize().x) / 2),
				parentShell.getLocation().y
						+ ((parentControl.getSize().y - shell.getSize().y) / 2));
		shell.open();
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}

	private String getPatterns() {
		return this.patterns.getText();
	}

	private String getName() {
		return this.nameText.getText();
	}

	private void setErrorMessage(final String msg) {
		this.errorLabel.setText(msg);
	}

	private void close() {
		this.shell.setVisible(false);
		this.shell.dispose();
	}

	protected void save() {

		Collection<String> newPatterns = new LinkedList<String>();
		for (String pattern : getPatterns().split("[\\r\\n]+")) {
			pattern = pattern.trim();
			if (!pattern.isEmpty()) {
				try {
					Pattern.compile(pattern);
					newPatterns.add(pattern);

				} catch (PatternSyntaxException e) {
					setErrorMessage("Invalid regular expression pattern: "
							+ pattern);
					int startOfPattern = getPatterns().indexOf(pattern);
					this.patterns.setSelection(startOfPattern, startOfPattern
							+ pattern.length());
					return;
				}

			}
		}

		this.resultingName = this.getName();
		filterRepo().putFilter(this.resultingName, newPatterns);
		this.close();
	}

	private FrameFilterRepository filterRepo() {
		return DecentipedePlugin.getDefault().getFrameFilterRepository();
	}

	public String getResultingName() {
		return resultingName;
	}

}
