/*
 * Mutability Detector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * Further licensing information for this project can be found in 
 * 		license/LICENSE.txt
 */
package org.mutabilitydetector;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;

import java.io.IOException;
import java.io.InputStream;

import org.mutabilitydetector.IAnalysisSession.AnalysisError;
import org.mutabilitydetector.checkers.IMutabilityChecker;
import org.objectweb.asm.ClassReader;


import com.google.classpath.ClassPath;

public class CheckerRunner {

	private ClassReader cr;
	private final ClassPath classpath;

	public CheckerRunner(ClassPath classpath) {
		this.classpath = classpath;
	}

	public void run(IMutabilityChecker checker, Class<?> toCheck) {
		try {
			cr = new ClassReader(toCheck.getName());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		cr.accept(checker, 0);
	}

	public void run(IAnalysisSession analysisSession, IMutabilityChecker checker, String dottedClassPath) {
		try {
			try {
				Class<?> toCheck = currentThread().getContextClassLoader().loadClass(dottedClassPath);
				cr = new ClassReader(toCheck.getName());
				cr.accept(checker, 0);
			} catch (Throwable e) {
				// Has to catch NoClassDefFoundError
				analyseAsStream(checker, dottedClassPath);
			}
		} catch (Throwable e) {
			handleException(analysisSession, checker, dottedClassPath, e);
		}
	}

	private void analyseAsStream(IMutabilityChecker checker, String dottedClassPath) throws IOException {
		String slashedClassPath = dottedClassPath.replace(".", "/").concat(".class");
		InputStream classStream = classpath.getResourceAsStream(slashedClassPath);
		cr = new ClassReader(classStream);
		cr.accept(checker, 0);
	}

	private void handleException(IAnalysisSession analysisSession, IMutabilityChecker checker, String dottedClassPath, Throwable e) {
		String errorDescription = format("It is likely that the class %s has dependencies outwith the given class path.", dottedClassPath);
		AnalysisError error = new AnalysisError(dottedClassPath, getNameOfChecker(checker), errorDescription);
		analysisSession.addAnalysisError(error);
	}

	private String getNameOfChecker(IMutabilityChecker checker) {
		String checkerName = checker.getClass().getName();
		checkerName = checkerName.substring(checkerName.lastIndexOf(".") + 1);
		return checkerName;

	}
}