/*
 * Copyright 2009 Eugene Prokopiev <eugene.prokopiev@gmail.com>
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
package txm.lib.impl.ewsd;

import txm.lib.common.core.Command;
import txm.lib.common.core.Error;
import txm.lib.common.stream.SocketCommandManager;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class EwsdCommandManager extends SocketCommandManager {

	@Override
	public void connect() throws Error {
		super.connect();
		String result;
		read("<<<\n(Welcome to FOS Gateway.+)\n<<<", 1000);
		write(getAttribute("nm")+";"+getAttribute("login")+";RPPW-NO;"+getAttribute("password")+";\n");
		result = read("<<<\n(.+)\n<<<", 1000);
		if (!result.equals("Authorized the client"))
			throw new Error("Authorization error : "+result);
		write("GW-SET-UGNE: "+getAttribute("ug")+","+getAttribute("ne")+";\n");
		result = read("<<<\n(.+)\n<<<", 1000);
		if (!result.equals("Assigned UG & NE"))
			throw new Error("Authentication error : " + result);
	}

	@Override
	protected void run(Command command) throws Error {
		write("GW-TASK: "+command.getText()+";\n");
		command.setPullGroup(read("<<<\n"+command.getText().split(":")[0]+":(.+):TASK SUBMITTED\n<<<", 1000));
	}

	@Override
	public void pull(Command command) throws Error {
		super.pull(command);
		String[] result = read("<<<\n(.+:.+)\n<<<", 20000).split(":",2);
		if (result[0].equals(command.getPullGroup())) {
			command.addResult(result[1]);
		} else {
			unownedResults.add(result);
			pull(command);
		}
	}

}
