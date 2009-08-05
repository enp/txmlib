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

import java.util.Map;
import java.util.Properties;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public interface CommandManager {
	void connect(Properties params, CommandDump dump) throws CommandException;
	void execute(Command command) throws CommandException;
	void execute(Command command, Map<String,CommandExecution> resultMatch) throws CommandException;
	void execute(Command command, String pattern, CommandExecution execution) throws CommandException;
	void pull(Command command) throws CommandException;
	void pull(Command command, Map<String,CommandExecution> resultMatch) throws CommandException;
	void pull(Command command, String pattern, CommandExecution execution) throws CommandException;
	void disconnect() throws CommandException;
}
