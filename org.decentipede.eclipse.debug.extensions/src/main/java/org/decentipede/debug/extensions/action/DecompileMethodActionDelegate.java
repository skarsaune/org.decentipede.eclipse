package org.decentipede.debug.extensions.action;

import java.io.IOException;

import no.kantega.debug.decompile.ClassFileReverseEnginerer;

/**
 * Decompile source from byte codes of current method
 * 
 */
public class DecompileMethodActionDelegate extends DecompileActionDelegate {

	protected String decompile() throws Exception, IOException {
		return ClassFileReverseEnginerer.decompileMethod(this.fetchLocation);
	}
	
	

}
