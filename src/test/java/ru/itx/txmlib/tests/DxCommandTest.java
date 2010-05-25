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
import ru.itx.txmlib.common.core.Command;
import ru.itx.txmlib.common.core.CommandManager;
import ru.itx.txmlib.common.core.CommandResult;
import ru.itx.txmlib.common.core.CommandResultReader;
import ru.itx.txmlib.impl.dx.DxCommandManager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
public class DxCommandTest extends CommandTest {

	protected CommandManager getCommandManager() {
		return new DxCommandManager();
	}

	protected Map<Command, Map<String, CommandResultReader>> getCommands() {

		String device = getParam("device");

		Command command;
		Map<String,CommandResultReader> resultMatch;

		Map<Command,Map<String,CommandResultReader>> commands = new LinkedHashMap<Command,Map<String,CommandResultReader>>();

		command = new Command("RESET");
		commands.put(command, null);

		command = new Command("ZPLM:SUB="+device);
		resultMatch = new LinkedHashMap<String,CommandResultReader>();
		resultMatch.put("INCORRECT DIRECTORY NUMBER", new CommandResultReader() {
			public void read(CommandResult result) {
				System.out.println("{{{ INCORRECT NUMBER }}}");
			}
		});
		resultMatch.put("BUSY", new CommandResultReader() {
			public void read(CommandResult result) {
				System.out.println("{{{ BUSY }}}");
			}
		});
		resultMatch.put("NUMBER.+\r\n(.+)\r\n(.+"+device+".+)\r\n", new CommandResultReader() {
			public void read(CommandResult result) {
				System.out.println("{{{ units  : "+result.getAttribute("1")+"}}}");
				System.out.println("{{{ values : "+result.getAttribute("2")+"}}}");
			}
		});
		commands.put(command, resultMatch);

		return commands;
	}
}