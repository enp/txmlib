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

import java.util.LinkedHashMap;
import java.util.Map;

import tx.common.core.Command;
import tx.common.core.CommandResultReader;
import tx.common.core.CommandResult;
import tx.impl.dx.DxCommandManager;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class DxCommandExample {

	public static void main(String[] args) throws Exception {
		
		CommandExample commandExample = new CommandExample(new DxCommandManager());
		
		String phone = commandExample.getParam("device");
		
		Command command;
		Map<String,CommandResultReader> resultMatch;		
		
		Map<Command,Map<String,CommandResultReader>> commands = new LinkedHashMap<Command,Map<String,CommandResultReader>>();
		
		command = new Command("RESET");	
		commands.put(command, null);
		
		command = new Command("ZPLM:SUB="+phone);		
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
		resultMatch.put("NUMBER.+\r\n(.+)\r\n(.+"+phone+".+)\r\n", new CommandResultReader() {
			public void read(CommandResult result) {
				System.out.println("{{{ units  : "+result.getAttribute("1")+"}}}");
				System.out.println("{{{ values : "+result.getAttribute("2")+"}}}");
			}
		});	
		commands.put(command, resultMatch);
		
		commandExample.execute(commands);
		
	}

}
