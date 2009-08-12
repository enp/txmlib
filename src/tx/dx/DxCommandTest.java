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
package tx.dx;

import java.util.LinkedHashMap;
import java.util.Map;

import tx.common.CommandTest;
import tx.common.core.Command;
import tx.common.core.CommandExecution;
import tx.common.core.CommandResult;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class DxCommandTest extends CommandTest {

	public static void main(String[] args) throws Exception {
		
		String phone = "2631000";
		
		Command command;
		Map<String,CommandExecution> resultMatch;		
		
		Map<Command,Map<String,CommandExecution>> commands = new LinkedHashMap<Command,Map<String,CommandExecution>>();
		
		command = new Command("RESET");	
		commands.put(command, null);
		
		command = new Command("ZPLM:SUB="+phone);		
		resultMatch = new LinkedHashMap<String,CommandExecution>();
		resultMatch.put("BUSY", new CommandExecution() {
			public void executed(CommandResult result) {
				System.out.println("{{{ BUSY }}}");
			}
		});
		resultMatch.put("NUMBER.+\r\n(.+)\r\n(.+"+phone+".+)\r\n", new CommandExecution() {
			public void executed(CommandResult result) {
				System.out.println("{{{ units  : "+result.getAttribute("1")+"}}}");
				System.out.println("{{{ values : "+result.getAttribute("2")+"}}}");
			}
		});	
		commands.put(command, resultMatch);
		
		new DxCommandTest().execute(new DxCommandManager(), commands);
		
	}

}
