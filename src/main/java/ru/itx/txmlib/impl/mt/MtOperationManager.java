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
package ru.itx.txmlib.impl.mt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.itx.txmlib.common.core.Command;
import ru.itx.txmlib.common.core.CommandResult;
import ru.itx.txmlib.common.core.CommandResultReader;
import ru.itx.txmlib.common.core.Device;
import ru.itx.txmlib.common.core.Error;
import ru.itx.txmlib.common.core.Operation;
import ru.itx.txmlib.common.core.OperationManager;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
public class MtOperationManager extends OperationManager {

	public void linetest(final Operation operation) throws Error {
		
		for(final Device device : operation.getDevices()) {			
			executeCommand(operation, new Command("RESET"));
			executeCommand(operation, new Command("ESAB"),"ESSAI D\"UNE LIGNE D\"ABONNE",null);
			executeCommand(operation, new Command("ND="+device.getName()),"EXC",null);
			executeCommand(operation, new Command("PH=L"),"(.+L1.+)\r\nEXC",new CommandResultReader(){
				public void read(CommandResult result) {
					String names[] = new String[] {"AC A/G","AC B/G","DC A/G","DC B/G","R A/G","R B/G","R A/B","C A/B"};
					String values[] = result.getAttribute("1").split("\r\n");
					Pattern p = Pattern.compile("\\s+L\\d\\s+R = (\\S+)\\s+(\\S+)", Pattern.DOTALL);
					for(int i=0;i<8;i++) {
						Matcher m = p.matcher(values[i]);
						if (m.find())
							device.addAttribute(names[i], m.group(1)+" "+m.group(2));
					}
				}
			});
			executeCommand(operation, new Command("PH=FIN"),"EXC",null);
		}
		
	}

}
