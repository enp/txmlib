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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class OperationManager {
	
	@SuppressWarnings("unused")
	private long id;

	private List<Attribute> attributes;
	private CommandDump dump;
	
	private CommandManager commandManager;
	
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

	public void setDump(CommandDump dump) {
		this.dump = dump;
	}
	
	public void connect() throws Error {
		String commandManagerClass = this.getClass().getCanonicalName().replace("Operation", "Command");
		try {
			commandManager = (CommandManager)Class.forName(commandManagerClass).getConstructor(new Class[] {}).newInstance(new Object[] {});
			commandManager.setDump(dump);
			commandManager.setAttributes(attributes);
			commandManager.connect();
		} catch (Exception e) {
			throw new Error(e);
		}
		
	}

	public void execute(Operation operation) {
		for (Method method : this.getClass().getDeclaredMethods()) {
			if (method.getName().equals(operation.getAction())) {
				try {
					connect();
					method.invoke(this, operation);
				} catch (Exception e) {
					operation.setError(new Error(e));
				} finally {
					disconnect();
				}
				return;
			}
		}
		operation.setError(new Error(
			"Method ["+this.getClass().getCanonicalName()+"."+operation.getAction()+"] is not found"));
	}
	
	protected void executeCommand(Operation operation, Command command) throws Error {
		operation.addCommand(command);
		commandManager.execute(command);
	}

	protected void executeCommand(Operation operation, Command command, Map<String,CommandResultReader> resultMatch) throws Error {
		operation.addCommand(command);
		commandManager.execute(command, resultMatch);
	}

	protected void executeCommand(Operation operation, Command command, String pattern, CommandResultReader execution) throws Error {
		operation.addCommand(command);
		commandManager.execute(command, pattern, execution);
	}
	
	public void pullCommand(Command command, Map<String, CommandResultReader> resultMatch) throws Error {
		commandManager.pull(command, resultMatch);
	}
	
	public void disconnect() {
		try {
			commandManager.disconnect();
		} catch (Exception e) {
		}
	}
}
