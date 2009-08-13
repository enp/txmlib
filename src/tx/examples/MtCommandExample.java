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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import tx.common.core.Command;
import tx.common.core.CommandResultReader;
import tx.common.core.CommandResult;
import tx.impl.mt.MtCommandManager;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class MtCommandExample {

	public static void main(String[] args) throws Exception {
		
		CommandExample commandExample = new CommandExample(new MtCommandManager());
		
		String phone = commandExample.getParam("device");
		
		Command command;
		Map<String,CommandResultReader> resultMatch;		
		
		Map<Command,Map<String,CommandResultReader>> commands = new LinkedHashMap<Command,Map<String,CommandResultReader>>();
		
		command = new Command("RESET");	
		commands.put(command, null);
		
		command = new Command("ESAB");	
		resultMatch = new HashMap<String,CommandResultReader>();
		resultMatch.put("ESSAI D\"UNE LIGNE D\"ABONNE", null);
		commands.put(command, resultMatch);
		
		command = new Command("ND="+phone);	
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
		
		commandExample.execute(commands);
		
	}

}
