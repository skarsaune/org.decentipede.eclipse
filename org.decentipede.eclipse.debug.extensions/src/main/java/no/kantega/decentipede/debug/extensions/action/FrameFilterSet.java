package no.kantega.decentipede.debug.extensions.action;

import java.util.Collection;

public class FrameFilterSet implements Comparable<FrameFilterSet> {

	private final String name;
	private final Collection<String> exclusionPatterns;
	public static final String DELIMITER = ";";

	public FrameFilterSet(String name, Collection<String> exclusionPatterns) {
		super();
		this.name = name;
		this.exclusionPatterns = exclusionPatterns;
	}

	public Collection<String> getExcusionPatterns() {
		return this.exclusionPatterns;

	}

	public String getName() {
		return this.name;
	}

	@Override
	public int compareTo(FrameFilterSet o) {
		return name.compareTo(o.getName());
	}

	public String delimitedString() {

		final StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (final String pattern : this.exclusionPatterns) {
			if (first) {
				first = false;
			} else {
				builder.append(DELIMITER);
			}
			builder.append(pattern);
		}

		return builder.toString();
	}

}
