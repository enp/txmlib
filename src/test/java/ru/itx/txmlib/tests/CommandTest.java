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
package ru.itx.txmlib.tests;

import org.junit.Test;
import ru.itx.txmlib.common.core.*;

import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
abstract public class CommandTest {

	private Properties params = new Properties();
	private CommandManager commandManager = getCommandManager();

	abstract protected CommandManager getCommandManager();
	abstract protected Map<Command,Map<String,CommandResultReader>> getCommands();

	protected boolean pull() { return false; }

	protected String getParam(String param) {
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

	private void execute(Map<Command,Map<String,CommandResultReader>> commands, boolean pull) throws Exception {
		try {
			CommandDump dump = new CommandDump("dump/"+type(commandManager)+".txt");
			commandManager.setDump(dump);
			commandManager.setAttributes(params);
			commandManager.connect();
			if (commands != null) {
				for(Command command : commands.keySet()) {
					System.out.println("[ "+command.getText()+" ]");
					if (pull) {
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

	private void execute(Map<Command,Map<String,CommandResultReader>> commands) throws Exception {
		execute(commands, false);
	}

	@Test
	public void run() {
		try {
			String resource = "conf/"+type(commandManager)+".conf";
			URL url = ClassLoader.getSystemResource(resource);
			if (url != null) {
				params.load(url.openStream());
				String enable = getParam("enable");
				if (enable != null && enable.equals("yes"))
					execute(getCommands(), pull());
			} else {
				System.out.println("Error loading resource "+resource);
			}
		} catch (Exception e) {
			System.out.println("ERROR:");
			e.printStackTrace();
		}
	}
}
