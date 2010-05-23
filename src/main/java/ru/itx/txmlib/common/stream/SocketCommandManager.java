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
package ru.itx.txmlib.common.stream;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import ru.itx.txmlib.common.core.Error;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
public abstract class SocketCommandManager extends StreamCommandManager {

	private Socket socket = new Socket();
	
	@Override
	public void connect() throws Error {
		try {
			InetAddress addr = InetAddress.getByName(getAttribute("host"));
			int port = Integer.parseInt(getAttribute("port"));
			socket.connect(new InetSocketAddress(addr, port), 3000);
			is = socket.getInputStream();
			os = socket.getOutputStream();
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	@Override
	public void disconnect() throws Error {
		super.disconnect();
		try {
			socket.close();
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	@Override
	protected void setTimeout(int timeout) throws IOException {
		socket.setSoTimeout(timeout);
	}

}
