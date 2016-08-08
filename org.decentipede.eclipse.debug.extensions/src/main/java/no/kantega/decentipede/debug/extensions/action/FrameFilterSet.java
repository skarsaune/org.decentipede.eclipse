package no.kantega.decentipede.debug.extensions.action;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

import org.decentipede.eclipse.debug.core.DecentipedePlugin;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;

public class FrameFilterSet extends ToggleableFilter implements Comparable<FrameFilterSet> , FrameFilter  {

	private final String name;
	private final Map<String, Pattern> exclusionPatterns;
	public static final String DELIMITER = ";";

	public FrameFilterSet(String name, Collection<String> exclusionPatterns, boolean filterActive) {
		super();
		this.name = name;
		this.exclusionPatterns = compile(exclusionPatterns);
		this.filterActive = filterActive;
	}



	private static Map<String, Pattern> compile(final Collection<String> patterns) {
		
		final Map<String, Pattern> compiled = new HashMap<String, Pattern>();
		for (final String pattern : patterns) {
			compiled.put(pattern, Pattern.compile(pattern));
		}
		return compiled;
	}



	public Map<String, Pattern> getExcusionPatterns() {
		return this.exclusionPatterns;

	}

	public String name() {
		return this.name;
	}

	@Override
	public int compareTo(FrameFilterSet o) {
		return name.compareTo(o.name());
	}

	public String delimitedString() {

		final StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (final String pattern : this.exclusionPatterns.keySet()) {
			if (first) {
				first = false;
			} else {
				builder.append(DELIMITER);
			}
			builder.append(pattern);
		}

		return builder.toString();
	}

	@Override
	public boolean filter(IJavaStackFrame frame, String asString) {
		if(!this.isFilterActive()) {
			return true;
		}
		for (final Pattern pattern : this.exclusionPatterns.values()) {
			if(pattern.matcher(asString).matches()) {
				return false;
			}
		}
		return true;
	}
	
	void save() {
		IPreferenceStore store = DecentipedePlugin.getDefault().getPreferenceStore();
		store.setValue("filter." + name() + ".name", name());
		store.setValue("filter." + name() + ".exclusions", delimitedString());
		store.setValue("filter." + name() + ".enabled", isFilterActive());
		try {
			DecentipedePlugin.getDefault().getPreferenceStore().save();
		} catch (IOException e) {
		}
	}
	
	public static FrameFilterSet load(final String name) {
		IPreferenceStore store = DecentipedePlugin.getDefault().getPreferenceStore();
		String exclusionPatterns= store.getString("filter." + name + ".exclusions");
		boolean filterActive = store.getBoolean("filter." + name + ".enabled");
		return new FrameFilterSet(name, parseExclusionPatterns(exclusionPatterns), filterActive);
	}
	
	private static Collection<String> parseExclusionPatterns(
			final String patterns) {
		LinkedList<String> patternList = new LinkedList<String>();
		for (String pattern : patterns.split(FrameFilterSet.DELIMITER)) {
			patternList.add(pattern);
		}
		return patternList;
	}
	
	@Override
	public boolean toggleFilter() {
		boolean state = super.toggleFilter();
		this.save();
		return state;
		
	}
	


}
