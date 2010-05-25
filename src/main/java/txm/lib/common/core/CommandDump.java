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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class CommandDump {
	
	@SuppressWarnings("unused")
	private long id;
	
	private String fileName;
	private FileOutputStream fos;
	
	public CommandDump() {}
	
	public CommandDump(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		this.fos = new FileOutputStream(fileName);
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void write(int b) throws IOException {
		fos.write(b);
	}
	
	public void write(byte b) throws IOException {
		fos.write(b);
	}
	
	public void write(byte[] b) throws IOException {
		fos.write(b);
	}
	
	public void write(String s) throws IOException {
		fos.write(s.getBytes());
	}
	
	public void close() throws IOException {
		fos.close();
	}
	
}
