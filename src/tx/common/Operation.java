/*
 * Copyright 2009 Eugene Prokopiev <eugene.prokopiev@gmail.com>
 * 
 * This file is part of TXManager (Telephone eXchange Manager).
 *
 * TXManager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TXManager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TXManager. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package tx.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class Operation {

	private String action;
	private Map<String,Map<String,String>> devices = new HashMap<String,Map<String,String>>();
	private Map<String,String> options = new HashMap<String,String>();
	private List<Command> commands = new ArrayList<Command>();
	private OperationException exception;
	private OperationDump operationDump;
	private CommandDump commandDump;

	public Operation() {}
	
	public Operation(String action, Map<String,Map<String,String>> devices, Map<String, String> options) {
		this.action = action;
		this.devices = devices;
		this.options = options;
	}	
	
	public String getAction() {
		return action;
	}
	
	public Map<String,Map<String,String>> getDevices() {
		return devices;
	}
	
	public Map<String, String> getOptions() {
		return options;
	}
	
	public void addCommand(Command command) {
		if (command != null)
			commands.add(command);
	}
	
	public List<Command> getCommands() {
		return Collections.unmodifiableList(commands);
	}
	
	public void setException(OperationException e) {
		this.exception = e;		
	}
	
	public OperationException getException() {
		return exception;
	}

	public CommandDump getCommandDump() {
		return commandDump;
	}

	public void setCommandDump(CommandDump commandDump) {
		this.commandDump = commandDump;
	}

	public OperationDump getOperationDump() {
		return operationDump;
	}

	public void setOperationDump(OperationDump operationDump) {
		this.operationDump = operationDump;
	}
	
}
