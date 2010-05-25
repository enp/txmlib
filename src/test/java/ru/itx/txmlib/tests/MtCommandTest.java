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

import ru.itx.txmlib.common.core.Command;
import ru.itx.txmlib.common.core.CommandManager;
import ru.itx.txmlib.common.core.CommandResult;
import ru.itx.txmlib.common.core.CommandResultReader;
import ru.itx.txmlib.impl.dx.DxCommandManager;
import ru.itx.txmlib.impl.mt.MtCommandManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
public class MtCommandTest extends CommandTest {

	protected CommandManager getCommandManager() {
		return new MtCommandManager();
	}

	protected Map<Command, Map<String, CommandResultReader>> getCommands() {

		String device = getParam("device");

		Command command;
		Map<String,CommandResultReader> resultMatch;

		Map<Command,Map<String,CommandResultReader>> commands = new LinkedHashMap<Command,Map<String,CommandResultReader>>();

		command = new Command("RESET");
		commands.put(command, null);

		command = new Command("ESAB");
		resultMatch = new HashMap<String,CommandResultReader>();
		resultMatch.put("ESSAI D\"UNE LIGNE D\"ABONNE", null);
		commands.put(command, resultMatch);

		command = new Command("ND="+device);
		resultMatch = new HashMap<String,CommandResultReader>();
		resultMatch.put("EXC", null);
		commands.put(command, null);

		command = new Command("PH=L");
		resultMatch = new LinkedHashMap<String,CommandResultReader>();
		resultMatch.put("(.+L1.+)\r\nEXC", new CommandResultReader() {
			public void read(CommandResult result) {
				System.out.println(result.getAttributes().toString().replace(" ", ""));
			}
		});
		/*resultMatch.put("L1.+R = (.+)\r\n.+L2.+R = (.+)\r\n.+L3.+R = (.+)\r\n.+L4.+R = (.+)\r\n.+L5.+R = (.+)\r\n.+L6.+R = (.+)\r\n.+L7.+R = (.+)\r\n.+L8.+R = (.+)\r\nEXC", new CommandExecution() {
			public void executed(CommandResult result) {
				System.out.println(result.getAttributes().toString().replace(" ", ""));
			}
		});*/
		commands.put(command, resultMatch);

		command = new Command("PH=FIN");
		resultMatch = new HashMap<String,CommandResultReader>();
		resultMatch.put("EXC", null);
		commands.put(command, resultMatch);

		return commands;
	}
}