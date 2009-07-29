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
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public abstract class StreamCommandManager implements CommandManager {
	
	protected Properties params;
	
	protected StreamCommandDump dump;
	
	protected InputStream is;
	protected OutputStream os;
	
	private long currentChar = 0;
	
	@Override
	public void connect(Properties params, CommandDump dump) throws CommandException {
		this.params = params;
		this.dump = (StreamCommandDump)dump;
	}
	
	@Override
	public void execute(Command command) throws CommandException {
		if (dump != null) command.setDump(dump);
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

	protected abstract void run(Command command) throws CommandException;

	protected void reset(Command command) throws CommandException {}
	
	@Override
	public void disconnect() throws CommandException {
		try {
			if (dump != null) dump.close();
		} catch (IOException e) {
			throw new CommandException(e);
		}
	}

	protected void write(String text) throws CommandException {
		try {
			os.write(text.getBytes());
		} catch (IOException e) {
			throw new CommandException(e);
		}
	}
	
	protected void write(byte[] bytes) throws CommandException {
		try {
			os.write(bytes);
		} catch (IOException e) {
			throw new CommandException(e);
		}
	}
	
	protected void write(int b) throws CommandException {
		try {
			os.write(b);
		} catch (IOException e) {
			throw new CommandException(e);
		}
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
	
	protected StreamCommandResult read(String[] patterns, int timeout, boolean exception) throws CommandException {
		int c;
		long beginChar = currentChar;
        StringBuilder buffer = new StringBuilder();
		try {
			setTimeout(timeout);
			while((c = is.read()) != -1) {
				buffer.append((char)c);
				if (dump != null) dump.write(c);
				currentChar++;				
				for (int i=0; i<patterns.length; i++) {
	            	Pattern pattern = Pattern.compile(patterns[i], Pattern.DOTALL);
	            	Matcher matcher = pattern.matcher(buffer);
	            	if (matcher.find())
	            		if (matcher.groupCount() > 0)
	            			return new StreamCommandResult(i, matcher.group(1), beginChar, currentChar);
	            		else
	            			return new StreamCommandResult(i, "", beginChar, currentChar);
	            }
			}
		} catch (SocketTimeoutException e) {
			// go end
		} catch (IOException e) {
			new CommandException(e);
		}
		if (exception)
			throw new StreamCommandException(patterns, buffer.toString(), beginChar, currentChar);
		else
			return null;
	}
	
	protected StreamCommandResult read(String[] patterns, int timeout) throws CommandException {
		return read(patterns, timeout, true);
	}
	
	protected StreamCommandResult read(String pattern, int timeout, boolean exception) throws CommandException {
		return read(new String[] { pattern }, timeout, exception);
	}
	
	protected StreamCommandResult read(String pattern, int timeout) throws CommandException {
		return read(new String[] { pattern }, timeout, true);
	}
	
	protected StreamCommandResult read(byte b, int timeout, boolean exception) throws CommandException {
		return read(new String( new byte[] { b }), timeout, exception);
	}
	
	protected StreamCommandResult read(byte b, int timeout) throws CommandException {
		return read(new String( new byte[] { b }), timeout, true);
	}

}
