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
package ru.itx.txmlib.impl.dx;

import java.util.HashMap;
import java.util.Map;

import ru.itx.txmlib.common.core.Command;
import ru.itx.txmlib.common.core.Error;
import ru.itx.txmlib.common.stream.SocketCommandManager;
import ru.itx.txmlib.common.stream.StreamReader;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
public class DxCommandManager extends SocketCommandManager {
	
	public void reset(Command command) throws Error {
		write(0x19);
		Map<String,StreamReader> resultMatch = new HashMap<String,StreamReader>();
		resultMatch.put("(END OF DIALOGUE SESSION)\r\n\b\n", null);
		resultMatch.put("([^\n]+>)\r\n< ", null);
		String result = read(resultMatch, 1000, false);
		command.addResult(result);
		if (result == null || result.equals("END OF DIALOGUE SESSION")) {
			write(new byte[] { 0x0d, 0x00 });
			result = read("(ENTER PASSWORD) < \b", 1000, false);
			command.addResult(result);
			if (result == null) {					// first password attempt fails sometimes  
				write(new byte[] { 0x0d, 0x00 });
				command.addResult(read("(ENTER PASSWORD) < \b", 1000));
			}
			write(getAttribute("password")+"\r");
			command.addResult(read("([^\n]+>)\r\n< ", 1000));
		}
	}

	@Override
	public void run(Command command) throws Error {
		write(command.getText()+";\r");
		command.addResult(read("\r\n(\\S.+\\S)\\s+\r\n.+>\r\n< ", 30000));
	}

}
