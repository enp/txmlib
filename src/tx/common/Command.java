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

	private List<CommandResult> results = new ArrayList<CommandResult>();
	
	private List<CommandError> errors = new ArrayList<CommandError>();
	
	private CommandManagerDump dump;

	public Command(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
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
	
	public void addResult(StreamReadResult result) {
		if (result != null)
			results.add(new CommandResult(result));
	}

	public void addError(StreamReadException e) {
		errors.add(new CommandError(e));		
	}

	public void addDump(CommandManagerDump dump) {
		this.dump = dump;
	}
	
	public String getDumpFileName() {
		return dump.getFileName();
	}

}
