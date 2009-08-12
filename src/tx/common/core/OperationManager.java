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
package tx.common.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import tx.common.CommandException;
import tx.common.OperationException;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class OperationManager {
	
	protected CommandManager commandManager;
	
	public void connect(Properties params, CommandDump commandDump) throws OperationException {
		String commandManagerClass = this.getClass().getCanonicalName().replace("Operation", "Command");
		try {
			commandManager = (CommandManager)Class.forName(commandManagerClass).getConstructor(new Class[] {}).newInstance(new Object[] {});
			commandManager.connect(params, commandDump);
		} catch (Exception e) {
			throw new OperationException(e);
		}
		
	}

	public void execute(Operation operation) {
		for (Method method : this.getClass().getDeclaredMethods()) {
			if (method.getName().equals(operation.getAction())) {
				try {
					method.invoke(this, operation);
				} catch (Exception e) {
					operation.setException(new OperationException(e));
				}
				return;
			}
		}
		operation.setException(new OperationException(
			"Method ["+this.getClass().getCanonicalName()+"."+operation.getAction()+"] is not found"));
	}
	
	protected void executeCommand(Operation operation, Command command) throws CommandException {
		operation.addCommand(command);
		commandManager.execute(command);
	}

	protected void executeCommand(Operation operation, Command command, Map<String,CommandExecution> resultMatch) throws CommandException {
		operation.addCommand(command);
		commandManager.execute(command, resultMatch);
	}

	protected void executeCommand(Operation operation, Command command, String pattern, CommandExecution execution) throws CommandException {
		operation.addCommand(command);
		commandManager.execute(command, pattern, execution);
	}
	
	public void pullCommand(Command command, Map<String, CommandExecution> resultMatch) throws CommandException {
		commandManager.pull(command, resultMatch);
	}
	
	public void disconnect() throws OperationException {
		try {
			commandManager.disconnect();
		} catch (Exception e) {
			throw new OperationException(e);
		}
	}
}
