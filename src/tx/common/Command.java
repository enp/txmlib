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
import java.util.Collections;
import java.util.List;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class Command {
	
	private String text;

	private String number;
	
	private List<CommandResult> results = new ArrayList<CommandResult>();
	
	private CommandDump dump;
	
	private CommandException exception;

	public Command(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public void setNumber(String number) {
		this.number = number;		
	}

	public String getNumber() {
		return number;
	}
	
	public void addResult(String result) {
		if (result != null)
			results.add(new CommandResult(result));
	}
	
	public void addResult(CommandResult result) {
		if (result != null)
			results.add(result);
	}

	public CommandResult getResult() {
		if (results.size() > 0)
			return results.get(results.size()-1);
		else
			return null;
	}
	
	public List<CommandResult> getResults() {
		return Collections.unmodifiableList(results);
	}

	public CommandDump getDump() {
		return dump;
	}

	public void setDump(CommandDump dump) {
		this.dump = dump;
	}

	public void setException(CommandException e) {
		this.exception = e;		
	}
	
	public CommandException getException() {
		return exception;
	}

}
