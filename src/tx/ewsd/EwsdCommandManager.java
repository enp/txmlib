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
package tx.ewsd;

import java.util.Properties;

import tx.common.core.Command;
import tx.common.core.CommandDump;
import tx.common.core.Error;
import tx.common.stream.SocketCommandManager;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class EwsdCommandManager extends SocketCommandManager {

	@Override
	public void connect(Properties params, CommandDump dump) throws Error {
		super.connect(params, dump);
		String result;
		read("<<<\n(Welcome to FOS Gateway.+)\n<<<", 1000);
		write(params.get("nm")+";"+params.get("login")+";RPPW-NO;"+params.get("password")+";\n");
		result = read("<<<\n(.+)\n<<<", 1000);
		if (!result.equals("Authorized the client"))
			throw new Error("Authorization error : "+result);
		write("GW-SET-UGNE: "+params.get("ug")+","+params.get("ne")+";\n");
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
