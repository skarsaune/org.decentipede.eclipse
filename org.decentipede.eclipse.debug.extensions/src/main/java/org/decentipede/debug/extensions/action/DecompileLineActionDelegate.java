package org.decentipede.debug.extensions.action;

import no.kantega.debug.decompile.ClassFileReverseEnginerer;

public class DecompileLineActionDelegate extends DecompileActionDelegate {

	@Override
	protected String decompile() throws Exception {
		return ClassFileReverseEnginerer.decompileCurrentLine(this.fetchLocation);
		}

}
