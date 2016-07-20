package org.decentipede.eclipse.debug.extensions.memory;

import org.eclipse.debug.core.ILogicalStructureProvider;
import org.eclipse.debug.core.ILogicalStructureType;
import org.eclipse.debug.core.model.IValue;

public class ReferencesToGcRootModel implements ILogicalStructureProvider {

	@Override
	public ILogicalStructureType[] getLogicalStructureTypes(IValue value) {
		value.getModelIdentifier();
		return new ILogicalStructureType[0];
	}

}
