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
package ru.itx.txmlib.common.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
public class Operation {

	private long id = 0;

	private OperationManager operationManager;
	
	private String action;
	private List<Device> devices = new ArrayList<Device>();
	private List<Attribute> attributes = new ArrayList<Attribute>();
	private List<Command> commands = new ArrayList<Command>();	
	private CommandDump dump;	
	private Error error;
	
	private Date beginTime;
	private Date endTime;
	
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
		return Collections.unmodifiableList(devices);
	}
	
	public List<Attribute> getAttributes() {
		return Collections.unmodifiableList(attributes);
	}
	
	public void addCommand(Command command) {
		if (command != null)
			commands.add(command);
	}
	
	public List<Command> getCommands() {
		return Collections.unmodifiableList(commands);
	}

	public void setDump() throws Exception {
		String dumpPath = operationManager.getDumpPath();
		if (dumpPath != null)
			dump = new CommandDump(operationManager.getDumpPath()+"/"+id);
		else
			throw new Exception("Dump path is null");
	}

	public void setDump(CommandDump dump) {
		this.dump = dump;
	}

	public CommandDump getDump() {
		return dump;
	}
	
	public void setError(Error e) {
		this.error = e;		
	}
	
	public Error getError() {
		return error;
	}
	
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void execute() {
		operationManager.execute(this);
	}
	
}
