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
import java.util.Collections;
import java.util.List;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class Operation {

	@SuppressWarnings("unused")
	private long id;

	private OperationManager operationManager;
	
	private String action;
	private List<Device> devices;
	private List<Attribute> attributes;
	private List<Command> commands;
	private Error error;

	public Operation() {}
	
	public Operation(OperationManager operationManager, String action, List<Device> devices, List<Attribute> attributes) {
		this.operationManager = operationManager;
		this.action = action;
		this.devices = devices;
		this.attributes = attributes;
	}	
	
	public String getAction() {
		return action;
	}
	
	public List<Device> getDevices() {
		return devices;
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	public void addCommand(Command command) {
		if (commands == null)
			commands = new ArrayList<Command>();
		if (command != null)
			commands.add(command);
	}
	
	public List<Command> getCommands() {
		return Collections.unmodifiableList(commands);
	}
	
	public void setError(Error e) {
		this.error = e;		
	}
	
	public Error getError() {
		return error;
	}
	
	public void execute() {
		operationManager.execute(this);
	}
	
}
