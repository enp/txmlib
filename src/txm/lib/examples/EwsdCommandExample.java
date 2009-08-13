/*
 * Copyright 2009 Eugene Prokopiev <eugene.prokopiev@gmail.com>
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
package txm.lib.examples;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import txm.lib.common.core.Command;
import txm.lib.common.core.CommandResult;
import txm.lib.common.core.CommandResultReader;
import txm.lib.impl.ewsd.EwsdCommandManager;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class EwsdCommandExample {

	public static void main(String[] args) throws Exception {
		
		CommandExample commandExample = new CommandExample(new EwsdCommandManager());
		
		String wst = commandExample.getParam("wst");
		String lac = commandExample.getParam("lac");
		String dn  = commandExample.getParam("dn");		
		
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
		commands.put(new Command("ACTWST:DN="+wst), resultMatch);
		
		resultMatch = new LinkedHashMap<String,CommandResultReader>();
		resultMatch.put("COMMAND SUBMITTED", null);
		resultMatch.put("EXEC'D", null);
		commands.put(new Command("STARTLTEST:LAC="+lac+",DN="+dn), resultMatch);
		
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
		
		commandExample.execute(commands, true);
		
	}

}
