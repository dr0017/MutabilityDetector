/*
 * Mutability Detector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * Further licensing information for this project can be found in 
 * 		license/LICENSE.txt
 */
package org.mutabilitydetector.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class OptionParserHelper {
	
	private Options options;
	private String[] args;

	public OptionParserHelper(Options options, String[] args) {
		this.options = options;
		this.args = args;
	}

	public void parseOptions(ParsingAction action) {
		CommandLineParser parser = new GnuParser();
		CommandLine line;
		try {
			line = parser.parse(options, args);
			action.doParsingAction(line);
		} catch (ParseException e) {
			System.err.println("Parsing command line failed.\nReason: " + e.getMessage());
			throw new RuntimeException(e);
		}
		
	}
}

interface ParsingAction {
	void doParsingAction(CommandLine line);
}