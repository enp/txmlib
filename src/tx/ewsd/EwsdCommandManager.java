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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import tx.common.Command;
import tx.common.CommandException;
import tx.common.PullCommandManager;
import tx.common.SocketCommandManager;
import tx.common.StreamCommandResult;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class EwsdCommandManager extends SocketCommandManager implements PullCommandManager {

	List<String[]> unownedResults = new ArrayList<String[]>();
	
	@Override
	public void connect(Properties params) throws CommandException {
		super.connect(params);
		StreamCommandResult result;
		read("<<<\n(Welcome to FOS Gateway.+)\n<<<", 1000);
		write(params.get("nm")+";"+params.get("login")+";RPPW-NO;"+params.get("password")+";\n");
		result = read("<<<\n(.+)\n<<<", 1000);
		if (!result.getText().equals("Authorized the client"))
			throw new CommandException("Authorization error : "+result.getText());
		write("GW-SET-UGNE: "+params.get("ug")+","+params.get("ne")+";\n");
		result = read("<<<\n(.+)\n<<<", 1000);
		if (!result.getText().equals("Assigned UG & NE"))
			throw new CommandException("Authentication error : " + result.getText());
	}

	@Override
	protected void run(Command command) throws CommandException {
		write("GW-TASK: "+command.getText()+";\n");
		command.setNumber(read("<<<\n"+command.getText().split(":")[0]+":(.+):TASK SUBMITTED\n<<<", 1000));
	}

	@Override
	public void pullResult(Command command) throws CommandException {
		for(String[] result : unownedResults) {
			if (result[0].equals(command.getNumber())) {
				command.addResult(result[1]);
				return;
			}
		}
		String[] result = read("<<<\n(.+:.+)\n<<<", 20000).getText().split(":",2);
		if (result[0].equals(command.getNumber())) {
			command.addResult(result[1]);
		} else {
			unownedResults.add(result);
			pullResult(command);
		}
	}

}
