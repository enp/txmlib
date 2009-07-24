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

import java.util.Properties;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class CommandTest {
	
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
	
	protected void executeCommands(CommandManager commandManager, Properties params, Command[] commands) throws CommandException {
		try {
			commandManager.connect(params);
			if (commands != null) {
				for(Command command : commands) {
					System.out.println("[ "+command.getText()+" ]");
					commandManager.execute(command);
					for(CommandResult result : command.getResults()) {
						System.out.println("<<<");
						System.out.println(result.getText());
						System.out.println(">>>");
					}
				}
			}
			commandManager.disconnect();
		} catch (StreamCommandException e) {
			processReadError(e);
		}
	}
}
