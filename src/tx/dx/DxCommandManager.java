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

import tx.common.CommandException;
import tx.common.CommandManager;
import tx.common.SocketCommandManager;
import tx.common.StreamCommandResult;
import tx.common.core.Command;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class DxCommandManager extends SocketCommandManager implements CommandManager {
	
	@Override
	public void reset(Command command) throws CommandException {
		write(0x19);
		StreamCommandResult result = read(
			new String[] { 
				"(END OF DIALOGUE SESSION)\r\n\b\n",	// need to enter password
				"([^\n]+>)\r\n< "						// command prompt is ready
			}, 1000, false);
		command.addResult(result);
		if (result == null || result.getIndex() == 0) {
			write(new byte[] { 0x0d, 0x00 });
			result = read("(ENTER PASSWORD) < \b", 1000, false);
			command.addResult(result);
			if (result == null) {					// first password attempt fails sometimes  
				write(new byte[] { 0x0d, 0x00 });
				command.addResult(read("(ENTER PASSWORD) < \b", 1000));
			}
			write(params.get("password")+"\r");
			command.addResult(read("([^\n]+>)\r\n< ", 1000));
		}
	}

	@Override
	public void run(Command command) throws CommandException {
		write(command.getText()+";\r");
		command.addResult(read("\r\n(\\S.+\\S)\\s+\r\n.+>\r\n< ", 30000));
	}

}
