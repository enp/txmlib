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
package tx.impl.mt;

import tx.common.core.Command;
import tx.common.core.Error;
import tx.common.stream.SocketCommandManager;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
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
