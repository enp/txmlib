/*
 * Copyright 2009-2010 Eugene Prokopiev <enp@itx.ru>
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
package ru.itx.txmlib.common.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.itx.txmlib.common.core.CommandManager;
import ru.itx.txmlib.common.core.Error;
import ru.itx.txmlib.common.core.MatchError;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
public abstract class StreamCommandManager extends CommandManager {
	
	protected InputStream is;
	protected OutputStream os;
	
	//private long currentChar = 0;
	
	@Override
	public void disconnect() throws Error {
		try {
			if (getDump() != null) getDump().close();
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	protected void write(String text) throws Error {
		try {
			os.write(text.getBytes());
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	
	protected void write(byte[] bytes) throws Error {
		try {
			os.write(bytes);
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	
	protected void write(int b) throws Error {
		try {
			os.write(b);
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	
	protected abstract void setTimeout(int timeout) throws IOException;
	
	protected void read(int timeout) throws IOException {
		setTimeout(timeout);
        try {
        	int c;
        	while((c = is.read()) != -1)
				if (getDump() != null) getDump().write(c);
		} catch (SocketTimeoutException e) {
			return;
		}
	}
	
	protected String read(Map<String,StreamReader> resultMatch, int timeout, boolean exception) throws Error {
		int c;
		//long beginChar = currentChar;
        StringBuilder buffer = new StringBuilder();
		try {
			setTimeout(timeout);
			while((c = is.read()) != -1) {
				buffer.append((char)c);
				if (getDump() != null) getDump().write(c);
				//currentChar++;
				for(String pattern : resultMatch.keySet()) {
					Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
	            	Matcher m = p.matcher(buffer);
	            	if (m.find()) {
	            		if (m.groupCount() > 0) {
	            			//resultMatch.get(pattern).read(m.group(1), beginChar, currentChar);
	            			if (resultMatch.get(pattern) != null)
	            				resultMatch.get(pattern).read(m.group(1));
	            			return m.group(1);
	            		} else {
	            			//resultMatch.get(pattern).read(m.group(), beginChar, currentChar);
	            			if (resultMatch.get(pattern) != null)
	            				resultMatch.get(pattern).read(m.group());
	            			return m.group();
	            		}
	            	}
				}
			}
		} catch (SocketTimeoutException e) {
			// go end
		} catch (IOException e) {
			new Error(e);
		}
		if (exception) {
			throw new MatchError(resultMatch.keySet(), buffer.toString());
		} else {
			return null;
		}
	}
	
	protected String read(String[] patterns, int timeout, boolean exception) throws Error {
		Map<String,StreamReader> resultMatch = new HashMap<String,StreamReader>();
		for (String pattern : patterns)
			resultMatch.put(pattern, null);
		return read(resultMatch, timeout, exception);
	}
	
	protected String read(String pattern, int timeout, boolean exception) throws Error {
		return read(new String[] { pattern }, timeout, exception);
	}
	
	protected String read(String pattern, int timeout) throws Error {
		return read(new String[] { pattern }, timeout, true);
	}
	
	protected String read(byte b, int timeout, boolean exception) throws Error {
		return read(new String( new byte[] { b }), timeout, exception);
	}
	
	protected String read(byte b, int timeout) throws Error {
		return read(new String( new byte[] { b }), timeout, true);
	}

}
