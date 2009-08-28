/*
 * Copyright 2009 Eugene Prokopiev <eugene.prokopiev@gmail.com>
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
package txm.lib.common.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public abstract class CommandManager {

	private List<Attribute> attributes = new ArrayList<Attribute>();	
	private CommandDump dump;
	
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public void setAttributes(Properties params) {
		attributes = new ArrayList<Attribute>();
		for(String name : params.stringPropertyNames())
			attributes.add(new Attribute(name, params.getProperty(name)));
	}
	
	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
	}
	
	public void addAttribute(String name, String value) {
		attributes.add(new Attribute(name, value));
	}
	
	protected String getAttribute(String name) {
		for(Attribute attribute : attributes)
			if (attribute.getName().equals(name))
				return attribute.getValue();
		return null;
	}
	
	public void setDump(CommandDump dump) {
		this.dump = dump;
	}
	
	public CommandDump getDump() {
		return dump;
	}
	
	public abstract void connect() throws Error;
	
	public abstract void disconnect() throws Error;

	protected void reset(Command command) throws Error {}
	
	public void execute(Command command) throws Error {
		try {
			command.setBeginTime(new Date());
			if (command.getText().equals("RESET"))
				reset(command);
			else
				run(command);
		} catch (Error e) {
			command.setError(e);
			throw e;
		} finally {
			command.setEndTime(new Date());
		}
	}
	
	protected abstract void run(Command command) throws Error;
	
	private void match(Command command, Map<String, CommandResultReader> resultMatch) throws Error {
		if (resultMatch != null) {
			CommandResult result = command.getResult();
			if (result != null) {
				for(String pattern : resultMatch.keySet()) {
					if (pattern == null)
						pattern = "";
					Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
		        	Matcher m = p.matcher(result.getText());
		        	if (m.find()) {
		        		for(int i=1; i<=m.groupCount(); i++)
		        			result.addAttribute(Integer.toString(i), m.group(i));
		        		if (resultMatch.get(pattern) != null)
		        			resultMatch.get(pattern).read(result);
		        		return;
		        	} 
				}
			}
			Error e = new MatchError(resultMatch.keySet(), result.getText());
			command.setError(e);
			throw e;
		}
	}
	
	public void execute(Command command, Map<String,CommandResultReader> resultMatch) throws Error {
		execute(command);
		match(command, resultMatch);
	}
	
	public void execute(Command command, String pattern, CommandResultReader execution) throws Error {
		Map<String,CommandResultReader> resultMatch = new HashMap<String,CommandResultReader>();
		resultMatch.put(pattern, execution);
		execute(command, resultMatch);
	}
	
	protected List<String[]> unownedResults = new ArrayList<String[]>();
	
	public void pull(Command command) throws Error {
		for(String[] result : unownedResults) {
			if (result[0].equals(command.getPullGroup())) {
				command.addResult(result[1]);
				return;
			}
		}
	}

	public void pull(Command command, Map<String, CommandResultReader> resultMatch) throws Error {
		for(String pattern : resultMatch.keySet())
			pull(command, pattern, resultMatch.get(pattern));
	}

	public void pull(Command command, String pattern, CommandResultReader execution) throws Error {
		pull(command);
		Map<String, CommandResultReader> resultMatch = new HashMap<String,CommandResultReader>();
		resultMatch.put(pattern, execution);
		match(command, resultMatch);
	}

}
