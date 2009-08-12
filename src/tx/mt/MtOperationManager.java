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
package tx.mt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tx.common.core.Command;
import tx.common.core.CommandExecution;
import tx.common.core.CommandResult;
import tx.common.core.Operation;
import tx.common.core.OperationManager;
import tx.common.core.Error;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class MtOperationManager extends OperationManager {

	public void linetest(final Operation operation) throws Error {
		
		for(final String device : operation.getDevices()) {			
			executeCommand(operation, new Command("RESET"));
			executeCommand(operation, new Command("ESAB"),"ESSAI D\"UNE LIGNE D\"ABONNE",null);
			executeCommand(operation, new Command("ND="+device),"EXC",null);
			executeCommand(operation, new Command("PH=L"),"(.+L1.+)\r\nEXC",new CommandExecution(){
				public void executed(CommandResult result) {
					String names[] = new String[] {"AC A/G","AC B/G","DC A/G","DC B/G","R A/G","R B/G","R A/B","C A/B"};
					String values[] = result.getAttribute("1").split("\r\n");
					Pattern p = Pattern.compile("\\s+L\\d\\s+R = (\\S+)\\s+(\\S+)", Pattern.DOTALL);
					for(int i=0;i<8;i++) {
						Matcher m = p.matcher(values[i]);
						if (m.find())
							operation.addResultEntry(device, names[i], m.group(1)+" "+m.group(2));							
					}
				}
			});
			executeCommand(operation, new Command("PH=FIN"),"EXC",null);
		}
		
	}

}
