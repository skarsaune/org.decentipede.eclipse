package no.kantega.decentipede.debug.extensions.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.ui.IDebugView;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


public class FilterFramesAction implements IObjectActionDelegate {

	private static Map<String, Pattern> exclusionPatterns=new HashMap<String, Pattern>();
	private static final ViewerFilter FILTER = new ViewerFilter(){ 

		@Override
		public boolean select(Viewer viewer, Object path, Object node) {
			if(node instanceof IJavaStackFrame)
			{
				try {
					final String typeName = ((IJavaStackFrame) node).getDeclaringTypeName();					
					
					if(exclusionPatterns.isEmpty())
						return true;

					final String stackFrameText = printStackFrame((IJavaStackFrame)node);

					for(final Pattern pattern : exclusionPatterns.values())
					{
						if(pattern.matcher(stackFrameText).matches())
							return false;
					}
				} catch (DebugException e) {
				}
			}
			return true;
		}

		private String printStackFrame(IJavaStackFrame node) throws DebugException {
			if(!node.getReceivingTypeName().equals(node.getDeclaringTypeName()))
				return String.format("%s(%s).%s(%s) line: %d", simpleName(node.getReceivingTypeName()), simpleName(node.getDeclaringTypeName()), node.getMethodName(), getArgumentTypes(node.getArgumentTypeNames()), node.getLineNumber());
			else
				return String.format("%s.%s(%s) line: %d", simpleName(node.getReceivingTypeName()),  node.getMethodName(), getArgumentTypes(node.getArgumentTypeNames()), node.getLineNumber());
			
		}

		private String getArgumentTypes(List<?> argumentTypeNames) {
			final StringBuilder builder=new StringBuilder();
			boolean first = true;
			for(final Object item : argumentTypeNames)
			{
				if(!first)
					builder.append(", ");
				else
					first = false;
				builder.append(simpleName(String.valueOf(item)));
			}
			return builder.toString();
		}

		private Object simpleName(String className) {
			final int finalDot = className.lastIndexOf('.');
			if(finalDot == -1)
				return className;
			else
				return className.substring(finalDot+1);
		}};
	private StructuredViewer viewer;
	private boolean shouldFilter;
	private final FrameFilterSet filterSet;
	
	/**
	 * Constructor for Action.
	 */
	public FilterFramesAction(FrameFilterSet filterSet) {
		super();
		this.filterSet = filterSet;
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

		if(targetPart instanceof IDebugView && targetPart.getClass().getSimpleName().equals("LaunchView"))
		{
			final Viewer partViewer = ((IDebugView)targetPart).getViewer();
			if(partViewer instanceof StructuredViewer)
			{
				this.viewer = (StructuredViewer) partViewer;
				addMyFilter();
			}
			
		}
		
	}

	protected void addMyFilter() {
		for(final ViewerFilter filter : this.viewer.getFilters())
		{
			//do not add my filter if already present
			if(filter == this.frameFilter())
				return;
		}
		this.viewer.addFilter(this.frameFilter());
	}

	private ViewerFilter frameFilter() {
		
		return FILTER;
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		toggleFilter(action);
		this.viewer.refresh();
	}

	protected void toggleFilter(IAction action) {
		shouldFilter=!shouldFilter;
		if(this.shouldFilter)
		{
			for(final String pattern : getExclusionPatterns())
			{
				exclusionPatterns.put(pattern, Pattern.compile(pattern));
			}
			
		}
		else
		{
			exclusionPatterns.keySet().removeAll(getExclusionPatterns());
		}
		action.setChecked(shouldFilter);
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
	
	protected Collection<String> getExclusionPatterns() {
		return this.filterSet.getExcusionPatterns();
	}
	
	protected static Collection<String> splitString(final String tokenString)
	{
		final Collection<String> strings=new LinkedList<String>();
		final StringTokenizer tok=new StringTokenizer(tokenString);
		while(tok.hasMoreTokens())
		{
			strings.add(tok.nextToken());
		}
		return strings;
	}
	

}