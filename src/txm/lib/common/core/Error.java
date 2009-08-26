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
package txm.lib.common.core;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class Error extends Exception {

	private static final long serialVersionUID = -3496293905790182660L;
	
	@SuppressWarnings("unused")
	private long id;
	
	private String trace;
	
	public Error() {}
	
	public Error(String message) {
		super(message);
		CharArrayWriter traceWriter = new CharArrayWriter();
        printStackTrace(new PrintWriter(traceWriter));
		trace = traceWriter.toString();
	}

	public Error(Exception e) {
		super(e);
		CharArrayWriter traceWriter = new CharArrayWriter();
        e.printStackTrace(new PrintWriter(traceWriter));
		trace = traceWriter.toString();
	}
	
	public String getTrace() {
		return trace;
	}

}
