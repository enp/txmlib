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
package tx.ewsd;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tx.common.CommandTest;
import tx.common.core.Command;
import tx.common.core.CommandResultReader;
import tx.common.core.CommandResult;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class EwsdCommandTest extends CommandTest {

	public static void main(String[] args) throws Exception {
		
		Map<Command,Map<String,CommandResultReader>> commands = new LinkedHashMap<Command,Map<String,CommandResultReader>>();
		
		Map<String,CommandResultReader> resultMatch;
		
		//resultMatch = new LinkedHashMap<String,CommandExecution>();
		//resultMatch.put("TASK SUBMITTED", null);
		//resultMatch.put("TASK EXECUTED", null);
		//resultMatch.put("TASK EXECUTED.+RCI-ST \n(\\S+)", new CommandExecution() {
		//	public void executed(CommandResult result) {
		//		System.out.println("{{{ "+result.getAttribute("1")+" }}}");
		//	}
		//});
		//resultMatch.put("TASK EXECUTED", new CommandExecution() {
		//	public void executed(CommandResult result) {
		//		Pattern p = Pattern.compile("RCI-ST \n(\\S+)", Pattern.DOTALL);
		//		Matcher m = p.matcher(result.getText());
		//		System.out.println("{{{ "+(m.find()?m.group(1):"not found")+" }}}");
		//	}
		//});
		//commands.put(new Command("DISPTIME"), resultMatch);
		
		resultMatch = new LinkedHashMap<String,CommandResultReader>();
		resultMatch.put("COMMAND SUBMITTED", null);
		resultMatch.put("ACCEPTED", null);
		resultMatch.put("EXEC'D", null);
		resultMatch.put("NEXT CALLTYPE FOR ACCEPTANCE", null);
		commands.put(new Command("ACTWST:DN=2687534"), resultMatch);
		
		resultMatch = new LinkedHashMap<String,CommandResultReader>();
		resultMatch.put("COMMAND SUBMITTED", null);
		resultMatch.put("EXEC'D", null);
		commands.put(new Command("STARTLTEST:LAC=8863,DN=2310550"), resultMatch);
		
		resultMatch = new LinkedHashMap<String,CommandResultReader>();
		resultMatch.put("COMMAND SUBMITTED", null);
		resultMatch.put("ACCEPTED", null);
		resultMatch.put("EXEC'D", new CommandResultReader() {
			public void read(CommandResult result) {
				Pattern p = Pattern.compile("RESULT OF LINE PARAM TEST\\s+(.+)\nEND TEXT", Pattern.DOTALL);
				Matcher m = p.matcher(result.getText());
				System.out.println("{{{ "+(m.find()?m.group(1):"not found")+" }}}");
			}
		});
		commands.put(new Command("TESTLINE:FCT=GT"), resultMatch);
		
		resultMatch = new LinkedHashMap<String,CommandResultReader>();
		resultMatch.put("COMMAND SUBMITTED", null);
		resultMatch.put("EXEC'D", null);
		commands.put(new Command("TESTLINE:FCT=GR"), resultMatch);
		
		resultMatch = new LinkedHashMap<String,CommandResultReader>();
		resultMatch.put("COMMAND SUBMITTED", null);
		resultMatch.put("EXEC'D", null);
		commands.put(new Command("DACTWST"), resultMatch);
		
		new EwsdCommandTest().execute(new EwsdCommandManager(), commands, true);
		
	}

}
