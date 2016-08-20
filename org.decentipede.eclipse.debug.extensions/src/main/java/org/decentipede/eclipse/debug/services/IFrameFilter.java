package org.decentipede.eclipse.debug.services;

import java.util.Collection;

import org.decentipede.debug.extensions.action.ToggleableFilter;

public interface IFrameFilter {

	Collection<? extends ToggleableFilter> allFilters();

	void forceSync();

	boolean isSelectionRelevant();

}
