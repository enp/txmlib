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

import java.io.CharArrayWriter;
import java.io.PrintWriter;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class CommandException extends Exception {

	private static final long serialVersionUID = 295539382772189883L;
	
	private String trace;
	
	public CommandException(String message) {
		super(message);
		CharArrayWriter traceWriter = new CharArrayWriter();
        printStackTrace(new PrintWriter(traceWriter));
		trace = traceWriter.toString();
	}

	public CommandException(Exception e) {
		super(e);
		CharArrayWriter traceWriter = new CharArrayWriter();
        e.printStackTrace(new PrintWriter(traceWriter));
		trace = traceWriter.toString();
	}
	
	public String getTrace() {
		return trace;
	}

}
