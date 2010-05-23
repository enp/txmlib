/*
 * Copyright 2009-2010 Eugene Prokopiev <enp@itx.ru>
 * 
 * This file is part of TXMLib (Telephone eXchange Management Library).
 *
 * TXMLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TXMLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TXMLib. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package ru.itx.txmlib.impl.ewsd;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.itx.txmlib.common.core.Command;
import ru.itx.txmlib.common.core.CommandResult;
import ru.itx.txmlib.common.core.CommandResultReader;
import ru.itx.txmlib.common.core.Device;
import ru.itx.txmlib.common.core.Error;
import ru.itx.txmlib.common.core.Operation;
import ru.itx.txmlib.common.core.OperationManager;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
public class EwsdOperationManager extends OperationManager {

	public void linetest(final Operation operation) throws Error {
		
		Command command;
		Map<String,CommandResultReader> resultMatch;		
		
		for(final Device device : operation.getDevices()) {			
			
			command = new Command("ACTWST:DN=2687534");			
			resultMatch = new LinkedHashMap<String,CommandResultReader>();
			resultMatch.put("COMMAND SUBMITTED", null);
			resultMatch.put("ACCEPTED", null);
			resultMatch.put("EXEC'D", null);
			resultMatch.put("NEXT CALLTYPE FOR ACCEPTANCE", null);
			executeCommand(operation, command);
			pullCommand(command, resultMatch);
			
			command = new Command("STARTLTEST:LAC=8863,DN="+device.getName());
			resultMatch = new LinkedHashMap<String,CommandResultReader>();
			resultMatch.put("COMMAND SUBMITTED", null);
			resultMatch.put("EXEC'D", null);
			executeCommand(operation, command);
			pullCommand(command, resultMatch);
			
			command = new Command("TESTLINE:FCT=GT");
			resultMatch = new LinkedHashMap<String,CommandResultReader>();
			resultMatch.put("COMMAND SUBMITTED", null);
			resultMatch.put("ACCEPTED", null);
			resultMatch.put("EXEC'D", new CommandResultReader() {
				public void read(CommandResult result) {
					Pattern p = Pattern.compile("--- \n(.+)\n\r\nEND TEXT", Pattern.DOTALL);
					Matcher m = p.matcher(result.getText());
					if (m.find()) {
						String[] names = new String[] {"DC","AC","R","C"};
						String[] rows = m.group(1).split("\n");
						for(int i=0;i<4;i++) {
							device.addAttribute(names[i]+" A/B", removeWhitespaces(rows[i].substring(5, 23).trim()));
							device.addAttribute(names[i]+" A/G", removeWhitespaces(rows[i].substring(25, 43).trim()));
							device.addAttribute(names[i]+" B/G", removeWhitespaces(rows[i].substring(45, 60).trim()));
						}
					}
				}
				private String removeWhitespaces(String source) {
					StringBuilder result = new StringBuilder();
					for(int i=0;i<source.length();i++) {
						if (source.charAt(i) != ' ' || source.charAt(i-1) != ' ')
							result.append(source.charAt(i));
					}
					return result.toString();
				}
			});
			executeCommand(operation, command);
			pullCommand(command, resultMatch);
			
			command = new Command("TESTLINE:FCT=GR");
			resultMatch = new LinkedHashMap<String,CommandResultReader>();
			resultMatch.put("COMMAND SUBMITTED", null);
			resultMatch.put("EXEC'D", null);
			executeCommand(operation, command);
			pullCommand(command, resultMatch);
			
			command = new Command("DACTWST");
			resultMatch = new LinkedHashMap<String,CommandResultReader>();
			resultMatch.put("COMMAND SUBMITTED", null);
			resultMatch.put("EXEC'D", null);
			executeCommand(operation, command);
			pullCommand(command, resultMatch);
			
		}
		
	}

}
