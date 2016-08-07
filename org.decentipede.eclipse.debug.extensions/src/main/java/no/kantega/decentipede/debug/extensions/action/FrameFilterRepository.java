package no.kantega.decentipede.debug.extensions.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.decentipede.eclipse.debug.core.DecentipedePlugin;
import org.decentipede.eclipse.debug.extensions.popup.actions.FilterSyntethicMethods;
import org.decentipede.eclipse.debug.extensions.preferences.PreferenceConstants;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.internal.ui.views.launch.LaunchView;
import org.eclipse.debug.ui.IDebugView;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

@SuppressWarnings("restriction")
public class FrameFilterRepository extends ViewerFilter implements ISelectionListener{
	
	
	private Viewer myViewer;
	
	private final Map<String, FrameFilterSet> repo = new TreeMap<String, FrameFilterSet>();

	private boolean stackFrameSelected;

	private void addFilter(FrameFilterSet filter) {
		this.repo.put(filter.name(), filter);
	}
	
	private void attach(Viewer viewer) {
		//I'm already attached, quit
		if(this.myViewer == viewer) {
			return;
		}
		StructuredViewer treeViewer = (StructuredViewer)viewer;
		
		for (ViewerFilter filter : treeViewer.getFilters()) {
			if(filter==this)//ensure we do not add ourselves more than once
				return;
		}
		treeViewer.addFilter(this);
	}

	public static FrameFilterRepository factoryDefaults() {
		FrameFilterRepository repository = new FrameFilterRepository();
		repository.addFilter(new FrameFilterSet(
				"Reflection method invocations", Arrays.asList(
						"java.lang.reflect.Method.*",
						"sun.reflect.DelegatingMethodAccessorImpl.*",
						"sun.reflect.NativeMethodAccessorImpl.*"), false));
		repository.addFilter(new FrameFilterSet(
				"Generated lambda bridges", Arrays.asList(
						".+\\$\\$Lambda\\$.+line: -1"), false));
		
		return repository;
	}

	private FrameFilterRepository() {

	}

	public FrameFilterRepository mergeWith(FrameFilterRepository repo2) {
		repo.putAll(repo2.repo);
		return this;
	}

	public String asPropertyString() throws IOException {
		//ensure all filters are saved at this point
		for (FrameFilterSet filterSet : this.repo.values()) {
			filterSet.save();
		}
		final StringBuilder delimited=new StringBuilder();
		boolean first = true;
		for (final String name : this.filterNames()) {
			if(first) {
				first=false;
			} else {
				delimited.append(',');
			}
			delimited.append(name);
		}
		return delimited.toString();
	}

	public static FrameFilterRepository fromPropertyString(
			final String propertyString) throws IOException {
		FrameFilterRepository repository = new FrameFilterRepository();
		for (String filterSetName : propertyString.split(";")) {
			if(filterSetName.trim().length() > 0) {
				repository.addFilter(FrameFilterSet.load(filterSetName));
			}
		}
		return repository;
	}



	public String[] filterNames() {
		return this.repo.keySet().toArray(new String[this.repo.size()]);
	}

	public void ensureFilters(String[] items) {
		this.repo.keySet().retainAll(Arrays.asList(items));
		//in case filters have been added / removed
		this.forceSync();

	}

	public void putFilter(String name, Collection<String> patterns) {

		this.repo.put(name, new FrameFilterSet(name, patterns, false));
	}

	public FrameFilterSet getFilter(final String existingName) {
		return this.repo.get(existingName);

	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object node) {

		if (node instanceof IJavaStackFrame) {
			String stackFrameText = null;
			for (ToggleableFilter filter : this.allFilters()) {
				if (filter.isFilterActive()) {
					try {
						if (stackFrameText == null) {
							stackFrameText = printStackFrame((IJavaStackFrame) node);
						}
						if (!filter.filter((IJavaStackFrame) node,
								stackFrameText)) {
							return false;
						}
					} catch (DebugException e) {
					}
				}
			}

		}
		return true;
	}

	private String printStackFrame(IJavaStackFrame node) throws DebugException {
		if (!node.getReceivingTypeName().equals(node.getDeclaringTypeName()))
			return String.format("%s(%s).%s(%s) line: %d",
					node.getReceivingTypeName(),
					node.getDeclaringTypeName(),
					node.getMethodName(),
					getArgumentTypes(node.getArgumentTypeNames()),
					node.getLineNumber());
		else
			return String.format("%s.%s(%s) line: %d",
					node.getReceivingTypeName(),
					node.getMethodName(),
					getArgumentTypes(node.getArgumentTypeNames()),
					node.getLineNumber());

	}

	private String getArgumentTypes(List<?> argumentTypeNames) {
		final StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (final Object item : argumentTypeNames) {
			if (!first)
				builder.append(", ");
			else
				first = false;
			builder.append(simpleName(String.valueOf(item)));
		}
		return builder.toString();
	}

	private Object simpleName(String className) {
		final int finalDot = className.lastIndexOf('.');
		if (finalDot == -1)
			return className;
		else
			return className.substring(finalDot + 1);
	}

	public Collection<? extends ToggleableFilter> allFilters() {

		final Collection<ToggleableFilter> filters = new LinkedList<ToggleableFilter>();
		filters.add(new FilterSyntethicMethods());
		filters.addAll(this.repo.values());
		return filters;
	}
	
	public void forceSync() {
		if(this.myViewer != null) {
			this.myViewer.refresh();
		}
		save();
	}

	private void save() {
		try {
			DecentipedePlugin.getDefault().getPreferenceStore().setValue(PreferenceConstants.P_STACK_FILTERS, this.asPropertyString());
		} catch (IOException e) {
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(part instanceof LaunchView) {
			this.attach(((IDebugView) part).getViewer());
			this.stackFrameSelected = (selection instanceof StructuredSelection) && ((StructuredSelection)selection).getFirstElement() instanceof IJavaStackFrame;
		}
	}
	
	public static String partIdOfConcern() {
		return "org.eclipse.debug.ui.DebugView";
	}
	
	public boolean isSelectionRelevant() {
		return this.stackFrameSelected;
	}
	
	@Override
	public boolean isFilterProperty(Object element, String property) {
		//very aggressive filtering strategy, to ensure we catch everything
		return true;
	}
}
