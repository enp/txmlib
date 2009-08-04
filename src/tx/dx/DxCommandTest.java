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

import tx.common.Command;
import tx.common.CommandExecution;
import tx.common.CommandResult;
import tx.common.CommandTest;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class DxCommandTest extends CommandTest {

	public static void main(String[] args) throws Exception {
		
		String phone = "2631000";
		
		Map<Command,Map<String,CommandExecution>> commands = new LinkedHashMap<Command,Map<String,CommandExecution>>();
		
		commands.put(new Command("RESET"), null);
		
		Command command = new Command("ZPLM:SUB="+phone);
		
		Map<String,CommandExecution> resultMatch = new LinkedHashMap<String,CommandExecution>();
		resultMatch.put("NUMBER.+\r\n(.+)\r\n(.+"+phone+".+)\r\n", new CommandExecution() {
			public void executed(CommandResult result) {
				System.out.println("{{{ units  : "+result.getAttribute("1")+"}}}");
				System.out.println("{{{ values : "+result.getAttribute("2")+"}}}");
			}
		});
		resultMatch.put("BUSY", new CommandExecution() {
			public void executed(CommandResult result) {
				System.out.println("{{{ BUSY }}}");
			}
		});
		
		commands.put(command, resultMatch);
		
		new DxCommandTest().execute(new DxCommandManager(), commands);
		
	}

}
