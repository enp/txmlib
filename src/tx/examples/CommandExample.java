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
package tx.examples;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

import tx.common.core.Command;
import tx.common.core.CommandDump;
import tx.common.core.CommandManager;
import tx.common.core.CommandResult;
import tx.common.core.CommandResultReader;
import tx.common.core.MatchError;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class CommandExample {
	
	private Properties params;
	private CommandManager commandManager;
	
	public CommandExample(CommandManager commandManager) throws Exception {
		this.commandManager = commandManager;
		params = new Properties();
		params.load(new FileInputStream("conf/"+type(commandManager)+".conf"));	
	}
	
	public String getParam(String param) {
		return (String)params.get(param);
	}
	
	private String type(CommandManager commandManager) {
		return commandManager.getClass().getSimpleName().replace("CommandManager", "").toLowerCase();
	}
	
	private void processMatchError(MatchError e) {
		System.err.println("<<<");
		System.err.println("COMMAND RESULT ERROR");
		System.err.println("-----------------");	
		System.err.println("Expected       : "+e.getExpected());
		System.err.println("Actual         : "+e.getActual());
		System.err.println(">>>");
		e.printStackTrace();
	}
	
	public void execute(Map<Command,Map<String,CommandResultReader>> commands, boolean pool) throws Exception {
		try {
			Properties params = new Properties();
			params.load(new FileInputStream("conf/"+type(commandManager)+".conf"));			
			CommandDump dump = new CommandDump("dump/"+type(commandManager)+".txt");
			commandManager.connect(params, dump);
			if (commands != null) {
				for(Command command : commands.keySet()) {
					System.out.println("[ "+command.getText()+" ]");
					if (pool) {
						commandManager.execute(command, null);
						commandManager.pull(command, commands.get(command));
					} else {
						commandManager.execute(command, commands.get(command));
					}
					for(CommandResult result : command.getResults()) {
						System.out.println("<<<");
						System.out.println(result.getText());
						System.out.println(">>>");
						if (result.getAttributes() != null) {
							System.out.println(result.getAttributes());
							System.out.println(">>>");
						}
					}
				}
			}
			commandManager.disconnect();
		} catch (MatchError e) {
			processMatchError(e);
		}
	}
	
	public void execute(Map<Command,Map<String,CommandResultReader>> commands) throws Exception {
		execute(commands, false);
	}
}
