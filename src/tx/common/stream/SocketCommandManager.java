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
package tx.common.stream;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;

import tx.common.CommandException;
import tx.common.core.CommandDump;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public abstract class SocketCommandManager extends StreamCommandManager {

	private Socket socket = new Socket();
	
	@Override
	public void connect(Properties params, CommandDump dump) throws CommandException {
		super.connect(params, dump);
		try {
			InetAddress addr = InetAddress.getByName((String)params.get("host"));
			int port = Integer.parseInt((String)params.get("port"));
			socket.connect(new InetSocketAddress(addr, port), 3000);
			is = socket.getInputStream();
			os = socket.getOutputStream();
		} catch (Exception e) {
			throw new CommandException(e);
		}
	}

	@Override
	public void disconnect() throws CommandException {
		super.disconnect();
		try {
			socket.close();
		} catch (Exception e) {
			throw new CommandException(e);
		}
	}
	
	@Override
	protected void setTimeout(int timeout) throws IOException {
		socket.setSoTimeout(timeout);
	}

}
