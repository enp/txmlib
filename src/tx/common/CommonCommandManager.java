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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public abstract class CommonCommandManager implements CommandManager {

	protected Properties params;
	
	protected abstract void setDump(CommandDump dump);
	protected abstract CommandDump getDump();
	
	protected abstract void run(Command command) throws CommandException;

	protected void reset(Command command) throws CommandException {}
	
	@Override
	public void connect(Properties params, CommandDump dump) throws CommandException {
		this.params = params;
		setDump(dump);
	}
	
	@Override
	public void execute(Command command) throws CommandException {
		command.setDump(getDump());
		try {
			if (command.getText().equals("RESET"))
				reset(command);
			else
				run(command);
		} catch (CommandException e) {
			command.setException(e);
			throw e;
		}
	}
	
	private void match(Command command, Map<String, CommandExecution> resultMatch) throws CommandException {
		if (resultMatch != null) {
			CommandResult result = command.getResult();
			if (result != null) {
				for(String pattern : resultMatch.keySet()) {
					if (pattern == null)
						pattern = "";
					Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
		        	Matcher m = p.matcher(result.getText());
		        	if (m.find()) {
		        		for(int i=1; i<=m.groupCount(); i++)
		        			result.addAttribute(Integer.toString(i), m.group(i));
		        		if (resultMatch.get(pattern) != null)
		        			resultMatch.get(pattern).executed(result);
		        		return;
		        	} 
				}
			}
			CommandException e = new MatchException(resultMatch.keySet(), result.getText());
			command.setException(e);
			throw e;
		}
	}
	
	@Override
	public void execute(Command command, Map<String,CommandExecution> resultMatch) throws CommandException {
		execute(command);
		match(command, resultMatch);
	}
	
	@Override
	public void execute(Command command, String pattern, CommandExecution execution) throws CommandException {
		Map<String,CommandExecution> resultMatch = new HashMap<String,CommandExecution>();
		resultMatch.put(pattern, execution);
		execute(command, resultMatch);
	}
	
	protected List<String[]> unownedResults = new ArrayList<String[]>();
	
	@Override
	public void pull(Command command) throws CommandException {
		for(String[] result : unownedResults) {
			if (result[0].equals(command.getPullGroup())) {
				command.addResult(result[1]);
				return;
			}
		}
	}

	@Override
	public void pull(Command command, Map<String, CommandExecution> resultMatch) throws CommandException {
		for(String pattern : resultMatch.keySet())
			pull(command, pattern, resultMatch.get(pattern));
	}

	@Override
	public void pull(Command command, String pattern, CommandExecution execution) throws CommandException {
		pull(command);
		Map<String, CommandExecution> resultMatch = new HashMap<String,CommandExecution>();
		resultMatch.put(pattern, execution);
		match(command, resultMatch);
	}

}
