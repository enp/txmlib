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
package tx.ewsd;

import tx.common.CommandManager;
import tx.common.CommonOperationManager;
import tx.common.Operation;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class EwsdOperationManager extends CommonOperationManager {

	CommandManager commandManager;
	
	public EwsdOperationManager() {
		commandManager = new EwsdCommandManager();
	}

	public void execute(Operation operation) {
		
		/*Command command = new Command("");
		
		try {
			commandManager.execute(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(commandManager.readNextResult(command)) {
			CommandResult commandResult = command.getResult();
			System.out.print(commandResult);
		}*/
	}

}
