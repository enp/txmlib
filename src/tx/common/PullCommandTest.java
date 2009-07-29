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

import java.util.Map;
import java.util.Properties;

public class PullCommandTest extends CommandTest {
	
	protected void executeCommands(PullCommandManager commandManager, Properties params, CommandDump dump, Map<Command,String[]> commands) throws CommandException {
		try {
			commandManager.connect(params, dump);
			if (commands != null) {
				for(Map.Entry<Command, String[]> entry : commands.entrySet()) {
					System.out.println("[ "+entry.getKey().getText()+" ]");
					commandManager.execute(entry.getKey());
					for(String pattern : entry.getValue()) {
						commandManager.pullResult(entry.getKey());
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
		} catch (StreamCommandException e) {
			processReadError(e);
		}
	}
}
