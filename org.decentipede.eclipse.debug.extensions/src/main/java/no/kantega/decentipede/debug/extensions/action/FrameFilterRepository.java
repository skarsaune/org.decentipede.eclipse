package no.kantega.decentipede.debug.extensions.action;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class FrameFilterRepository {
	private final Map<String, FrameFilterSet> repo = new TreeMap<String, FrameFilterSet>();

	private void addFilter(FrameFilterSet filter) {
		this.repo.put(filter.getName(), filter);
	}

	public static FrameFilterRepository factoryDefaults() {
		FrameFilterRepository repository = new FrameFilterRepository();
		repository.addFilter(new FrameFilterSet(
				"Reflection method invocations", Arrays.asList(
						"java.lang.reflect.Method.*",
						"sun.reflect.DelegatingMethodAccessorImpl.*",
						"sun.reflect.NativeMethodAccessorImpl.*")));
		return repository;
	}

	private FrameFilterRepository() {

	}

	public FrameFilterRepository mergeWith(FrameFilterRepository repo2) {
		repo.putAll(repo2.repo);
		return this;
	}

	public String asPropertyString() throws IOException {
		Properties properties = new Properties();
		int index = 1;
		for (FrameFilterSet frameFilterSet : repo.values()) {
			properties.setProperty("filter." + index + ".name",
					frameFilterSet.getName());
			properties.setProperty("filter." + index + ".exclusions",
					frameFilterSet.delimitedString());
			index++;
		}
		StringWriter writer = new StringWriter();
		properties.store(writer, "Exclusion filters");
		return writer.toString();
	}

	public static FrameFilterRepository fromPropertyString(
			final String propertyString) throws IOException {
		Properties properties = new Properties();
		properties.load(new StringReader(propertyString));
		int index = 1;
		String name = null;
		FrameFilterRepository repository = new FrameFilterRepository();
		while ((name = properties.getProperty("filter." + index + ".name")) != null) {
			final String patterns = properties.getProperty("filter." + index
					+ ".exclusions");
			repository.addFilter(new FrameFilterSet(name,
					parseExclusionPatterns(patterns)));
			index++;
		}
		return repository;
	}

	private static Collection<String> parseExclusionPatterns(
			final String patterns) {
		LinkedList<String> patternList = new LinkedList<String>();
		for (String pattern : patterns.split(FrameFilterSet.DELIMITER)) {
			patternList.add(pattern);
		}
		return patternList;
	}



	public String[] filterNames() {
		return this.repo.keySet().toArray(new String[this.repo.size()]);
	}

	public void ensureFilters(String[] items) {
		this.repo.keySet().retainAll(Arrays.asList(items));

	}

	public void putFilter(String name, Collection<String> patterns) {

		this.repo.put(name, new FrameFilterSet(name, patterns));
	}

	public FrameFilterSet getFilter(final String existingName) {
		return this.repo.get(existingName);
		
	}

	
}
