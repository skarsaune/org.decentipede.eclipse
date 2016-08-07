package no.kantega.decentipede.debug.extensions.action;

import org.eclipse.jdt.debug.core.IJavaStackFrame;

public interface FrameFilter {
	boolean filter(IJavaStackFrame frame, String asString);
}
