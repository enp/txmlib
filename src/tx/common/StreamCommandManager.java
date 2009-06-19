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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public abstract class StreamCommandManager implements CommandManager {
	
	protected Map<String,Object> params;
	
	protected StreamCommandManagerDump dump;
	
	protected InputStream is;
	protected OutputStream os;
	
	@Override
	public void connect(Map<String,Object> params) throws IOException {
		this.params = params;
		this.dump = ((StreamCommandManagerDump)params.get("dump"));
	}

	@Override
	public void execute(Command command) throws IOException {
		if (dump != null) command.addDump(dump);
		try {
			if (command.getText().equals("RESET"))
				reset(command);
			else
				run(command);
		} catch (StreamReadException e) {
			command.addError(e);
		}
	}

	protected abstract void run(Command command) throws IOException, StreamReadException;

	protected void reset(Command command) throws IOException, StreamReadException {}

	@Override
	public boolean readNextResult(Command command) {
		return false;
	}
	
	@Override
	public void disconnect() throws IOException {
		if (dump != null) dump.close();
	}

	protected void write(String text) throws IOException {
		os.write(text.getBytes());
	}
	
	protected void write(byte[] bytes) throws IOException {
		os.write(bytes);
	}
	
	protected void write(int b) throws IOException {
		os.write(b);
	}
	
	protected abstract void setTimeout(int timeout) throws IOException;
	
	protected void read(int timeout) throws IOException {
		setTimeout(timeout);
        try {
        	int c;
        	while((c = is.read()) != -1)
				if (dump != null) dump.write(c);
		} catch (SocketTimeoutException e) {
			return;
		}
	}
	
	protected StreamReadResult read(String[] patterns, int timeout, boolean exception) throws IOException, StreamReadException {
		int c;
        StringBuilder buffer = new StringBuilder();
		setTimeout(timeout);
		try {
			while((c = is.read()) != -1) {
				buffer.append((char)c);
				if (dump != null) dump.write(c);
	            for (int i=0; i<patterns.length; i++) {
	            	Pattern pattern = Pattern.compile(patterns[i], Pattern.DOTALL);
	            	Matcher matcher = pattern.matcher(buffer);
	            	if (matcher.find())
	            		if (matcher.groupCount() > 0)
	            			return new StreamReadResult(i, matcher.group(1), 0, 0);
	            		else
	            			return new StreamReadResult(i, "", 0, 0);
	            }
			}
		} catch (SocketTimeoutException e) {
			// go end
		}
		if (exception)
			throw new StreamReadException(buffer.toString(), 0, 0);
		else
			return null;
	}
	
	protected StreamReadResult read(String[] patterns, int timeout) throws IOException, StreamReadException {
		return read(patterns, timeout, true);
	}
	
	protected StreamReadResult read(String pattern, int timeout, boolean exception) throws IOException, StreamReadException {
		return read(new String[] { pattern }, timeout, exception);
	}
	
	protected StreamReadResult read(String pattern, int timeout) throws IOException, StreamReadException {
		return read(new String[] { pattern }, timeout, true);
	}
	
	protected StreamReadResult read(byte b, int timeout, boolean exception) throws IOException, StreamReadException {
		return read(new String( new byte[] { b }), timeout, exception);
	}
	
	protected StreamReadResult read(byte b, int timeout) throws IOException, StreamReadException {
		return read(new String( new byte[] { b }), timeout, true);
	}

}
