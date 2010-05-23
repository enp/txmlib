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

import ru.itx.txmlib.common.core.Command;
import ru.itx.txmlib.common.core.Error;
import ru.itx.txmlib.common.stream.SocketCommandManager;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
public class MtCommandManager extends SocketCommandManager {

	@Override
	public void reset(Command command) throws Error {
		write(0x01);
		command.addResult(read("@", 3000));
	}
	
	@Override
	public void run(Command command) throws Error {
		for(byte b : (command.getText()+":").getBytes()) {
			write(b);
			read(b, 3000);
		}			
		command.addResult(read("\r\n(.+)\r\n@", 30000));
	}

}
