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
	
	protected void processReadError(StreamCommandException e) {
		System.err.println("<<<");
		System.err.println("STREAM READ ERROR");
		System.err.println("-----------------");
		System.err.println("Dump positions : ["+e.getBegin() + "," + e.getEnd() + "]");			
		System.err.println("Expected       : "+e.getPatterns());
		System.err.println("Actual         : "+e.getText());
		System.err.println(">>>");
		e.printStackTrace();
	}
	
	protected void processCommandError(CommandResultException e) {
		System.err.println("<<<");
		System.err.println("COMMAND RESULT ERROR");
		System.err.println("-----------------");	
		System.err.println("Expected       : "+e.getPattern());
		System.err.println("Actual         : "+e.getText());
		System.err.println(">>>");
		e.printStackTrace();
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
	
	protected void execute(CommandManager commandManager, Command[] commands) throws Exception {
		try {
			Properties params = new Properties();
			params.load(new FileInputStream("conf/"+type(commandManager)+".conf"));			
			CommandDump dump = new TextFileCommandDump("dump/"+type(commandManager)+".txt");
			commandManager.connect(params, dump);
			if (commands != null) {
				for(Command command : commands) {
					System.out.println("[ "+command.getText()+" ]");
					commandManager.execute(command);
					for(CommandResult result : command.getResults()) {
						System.out.println("<<<");
						System.out.println(result.getText());
						System.out.println(">>>");
						if (result.getAttributes() != null) {
							for(String key : result.getAttributes().keySet())
								System.out.println("> "+key+" : "+result.getAttribute(key));
							System.out.println("> "+result.getAttributes());
						}
					}
				}
			}
			commandManager.disconnect();
		} catch (StreamCommandException e) {
			processReadError(e);
		}
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
		} catch (StreamCommandException e) {
			processReadError(e);
		} catch (CommandResultException e) {
			processCommandError(e);
		} catch (MatchException e) {
			processMatchError(e);
		}
	}
}
