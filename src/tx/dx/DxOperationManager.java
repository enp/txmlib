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
package tx.dx;

import java.util.LinkedHashMap;
import java.util.Map;

import tx.common.CommandException;
import tx.common.core.Command;
import tx.common.core.CommandExecution;
import tx.common.core.CommandResult;
import tx.common.core.Operation;
import tx.common.core.OperationManager;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class DxOperationManager extends OperationManager {

	public void linetest(final Operation operation) throws CommandException {
		
		for(final String device : operation.getDevices()) {
			
			executeCommand(operation, new Command("RESET"));
			
			Map<String,CommandExecution> resultMatch = new LinkedHashMap<String,CommandExecution>();
			resultMatch.put("BUSY", new CommandExecution() {
				public void executed(CommandResult result) {
					operation.addResultEntry(device, "STATE", "BUSY");
				}
			});
			resultMatch.put("NUMBER.+\r\n(.+)\r\n(.+"+device+".+)\r\n", new CommandExecution() {
				public void executed(CommandResult result) {
					operation.addResultEntry(device, "STATE", "IDLE");
					String[] names = new String[] {"AC A/G","AC B/G","DC A/G","DC B/G","R A/B","R A/G","R B/G","C A/B"};
					String[] units = result.getAttribute("1").split("\\s+");
					String[] values = result.getAttribute("2").split("\\s+");
					for(int i=0;i<8;i++)
						operation.addResultEntry(device, names[i], values[i+2]+" "+units[i+1]);
					operation.addResultEntry(device, "INTERFACE", values[10]);
				}
			});	
			
			executeCommand(operation, new Command("ZPLM:SUB="+device), resultMatch);
		}
		
	}

}
