/*
 * Mutability Detector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * Further licensing information for this project can be found in 
 * 		license/LICENSE.txt
 */
package org.mutabilitydetector.benchmarks;

public final class ImmutableWithIndirectlyConstructedFieldInConcreteType {

	@SuppressWarnings("unused")
	private final CharSequence name;
	
	public ImmutableWithIndirectlyConstructedFieldInConcreteType() {
		this.name = FieldFactory.getNewName();
	}
}