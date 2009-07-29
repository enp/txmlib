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

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class CommonOperationManager implements OperationManager {
	
	protected CommandManager commandManager;
	
	@Override
	public void connect(Properties params, CommandDump commandDump, OperationDump operationDump) throws OperationException {
		String commandManagerClass = this.getClass().getCanonicalName().replace("Operation", "Command");
		try {
			commandManager = (CommandManager)Class.forName(commandManagerClass).getConstructor(new Class[] {}).newInstance(new Object[] {});
			commandManager.connect(params, commandDump);
		} catch (Exception e) {
			throw new OperationException(e);
		}
		
	}

	@Override
	public void execute(Operation operation) {
		for (Method method : this.getClass().getDeclaredMethods()) {
			if (method.getName() == operation.getAction()) {
				try {
					method.invoke(this, operation.getDevices(), operation.getOptions());
					return;
				} catch (Exception e) {
					operation.setException(new OperationException(e));
					e.printStackTrace();
				}
			}
		}
		operation.setException(new OperationException(
			"Method ["+this.getClass().getCanonicalName()+"."+operation.getAction()+"] is not found"));
	}

	@Override
	public void disconnect() throws OperationException {
		try {
			commandManager.disconnect();
		} catch (Exception e) {
			throw new OperationException(e);
		}
	}
}
