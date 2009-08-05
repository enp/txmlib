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
package tx.common;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class CommandTest {
	
	protected String type(CommandManager commandManager) {
		return commandManager.getClass().getSimpleName().replace("CommandManager", "").toLowerCase();
	}
	
	protected void processMatchError(MatchException e) {
		System.err.println("<<<");
		System.err.println("COMMAND RESULT ERROR");
		System.err.println("-----------------");	
		System.err.println("Expected       : "+e.getExpected());
		System.err.println("Actual         : "+e.getActual());
		System.err.println(">>>");
		e.printStackTrace();
	}
	
	protected void execute(CommandManager commandManager, Map<Command,Map<String,CommandExecution>> commands) throws Exception {
		try {
			Properties params = new Properties();
			params.load(new FileInputStream("conf/"+type(commandManager)+".conf"));			
			CommandDump dump = new TextFileCommandDump("dump/"+type(commandManager)+".txt");
			commandManager.connect(params, dump);
			if (commands != null) {
				for(Command command : commands.keySet()) {
					System.out.println("[ "+command.getText()+" ]");
					commandManager.execute(command, commands.get(command));
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
		} catch (MatchException e) {
			processMatchError(e);
		}
	}
	
	protected void executeAndPull(CommandManager commandManager, Map<Command,String[]> commands) throws Exception {
		try {
			Properties params = new Properties();
			params.load(new FileInputStream("conf/"+type(commandManager)+".conf"));			
			CommandDump dump = new TextFileCommandDump("dump/"+type(commandManager)+".txt");
			commandManager.connect(params, dump);
			if (commands != null) {
				for(Map.Entry<Command, String[]> entry : commands.entrySet()) {
					System.out.println("[ "+entry.getKey().getText()+" ]");
					commandManager.execute(entry.getKey());
					for(String pattern : entry.getValue()) {
						commandManager.pull(entry.getKey());
						System.out.println("<<<");
						System.out.println(entry.getKey().getResult().getText());
						System.out.println(">>>");
						if (!entry.getKey().getResult().getText().contains(pattern)) {
							System.err.println("<<< pattern check failed >>>");
							System.exit(1);
						}
					}
				}
			}
			commandManager.disconnect();
		} catch (MatchException e) {
			processMatchError(e);
		}
	}
}
